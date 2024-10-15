package by.sorface.passport.web.services.users.providers;

import by.sorface.passport.web.config.options.ClientTokenOptions;
import by.sorface.passport.web.dao.models.OAuth2Client;
import by.sorface.passport.web.services.clients.OAuth2ClientService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplicationClientProvider implements RegisteredClientRepository {

    public static final String REDIRECT_URL_SPLITERATOR;

    static {
        REDIRECT_URL_SPLITERATOR = ";";
    }

    private final OAuth2ClientService oAuth2ClientService;

    private final ClientTokenOptions clientTokenOptions;

    @Override
    public void save(final RegisteredClient registeredClient) {
        final var oAuth2Client = new OAuth2Client();
        {
            oAuth2Client.setId(UUID.fromString(registeredClient.getId()));
            oAuth2Client.setClientId(registeredClient.getClientId());
            oAuth2Client.setClientName(registeredClient.getClientName());
            oAuth2Client.setClientSecret(registeredClient.getClientSecret());

            final var clientIdIssueAt = registeredClient.getClientIdIssuedAt() != null
                    ? LocalDateTime.ofInstant(registeredClient.getClientIdIssuedAt(), ZoneOffset.UTC)
                    : null;

            oAuth2Client.setClientIdIssueAt(clientIdIssueAt);

            final LocalDateTime clientSecretExpiresAt = registeredClient.getClientSecretExpiresAt() != null
                    ? LocalDateTime.ofInstant(registeredClient.getClientSecretExpiresAt(), ZoneOffset.UTC)
                    : null;

            oAuth2Client.setClientSecretExpiresAt(clientSecretExpiresAt);
            oAuth2Client.setRedirectUris(String.join(REDIRECT_URL_SPLITERATOR, registeredClient.getRedirectUris()));
        }

        oAuth2ClientService.save(oAuth2Client);
    }

    @SneakyThrows
    @Override
    public RegisteredClient findById(final String id) {
        final OAuth2Client oAuth2Client = oAuth2ClientService.findById(UUID.fromString(id));

        if (Objects.isNull(oAuth2Client)) {
            throw new AccessDeniedException("client not found");
        }

        return this.buildClient(oAuth2Client);
    }

    @SneakyThrows
    @Override
    public RegisteredClient findByClientId(final String clientId) {
        final OAuth2Client oAuth2Client = oAuth2ClientService.findByClientId(clientId);

        if (Objects.isNull(oAuth2Client)) {
            throw new AccessDeniedException("client not found");
        }

        return buildClient(oAuth2Client);
    }

    private RegisteredClient buildClient(final OAuth2Client oAuth2Client) {
        final var redirectUrls = this.getRedirectUrls(oAuth2Client.getRedirectUris());

        final var scopes = Set.of("scope.read", "scope.write", OidcScopes.OPENID, OidcScopes.PROFILE, OidcScopes.EMAIL);

        final ClientTokenOptions.TokenSetting accessTokenSettings = clientTokenOptions.getAccessToken();
        final ClientTokenOptions.TokenSetting authorizationCodeSettings = clientTokenOptions.getAuthorizationCode();

        Duration accessTokenTimeToLive = Duration.of(accessTokenSettings.getTimeToLiveValue(), accessTokenSettings.getTimeToLiveCron());
        final var tokenSettings = TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(accessTokenTimeToLive)
                .refreshTokenTimeToLive(Duration.ofSeconds(accessTokenTimeToLive.toSeconds() * 2))
                .reuseRefreshTokens(true)
                .authorizationCodeTimeToLive(Duration.of(authorizationCodeSettings.getTimeToLiveValue(), authorizationCodeSettings.getTimeToLiveCron()))
                .build();

        final var grantTypes = Set.of(
                AuthorizationGrantType.AUTHORIZATION_CODE,
                AuthorizationGrantType.CLIENT_CREDENTIALS,
                AuthorizationGrantType.REFRESH_TOKEN
        );

        final var clientsAuthMethod = List.of(
                ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                ClientAuthenticationMethod.CLIENT_SECRET_POST
        );

        return RegisteredClient
                .withId(oAuth2Client.getId().toString())
                .clientId(oAuth2Client.getClientId())
                .clientSecret(oAuth2Client.getClientSecret())
                .clientIdIssuedAt(oAuth2Client.getClientIdIssueAt().toInstant(ZoneOffset.UTC))
                .clientSecretExpiresAt(oAuth2Client.getClientSecretExpiresAt().toInstant(ZoneOffset.UTC))
                .clientName(oAuth2Client.getClientName())
                .clientAuthenticationMethods(clientAuthenticationMethods -> clientAuthenticationMethods.addAll(clientsAuthMethod))
                .authorizationGrantTypes(authorizationGrantTypes -> authorizationGrantTypes.addAll(grantTypes))
                .redirectUris(redirectUris -> redirectUris.addAll(redirectUrls))
                .scopes(scopeFunc -> scopeFunc.addAll(scopes))
                .tokenSettings(tokenSettings)
                .build();
    }

    private List<String> getRedirectUrls(final String value) {
        return Arrays.stream(value.split(ApplicationClientProvider.REDIRECT_URL_SPLITERATOR)).toList();
    }

}
