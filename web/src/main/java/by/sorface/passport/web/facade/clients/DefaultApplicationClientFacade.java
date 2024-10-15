package by.sorface.passport.web.facade.clients;

import by.sorface.passport.web.converters.OAuth2ClientConverter;
import by.sorface.passport.web.dao.models.OAuth2Client;
import by.sorface.passport.web.exceptions.NotFoundException;
import by.sorface.passport.web.exceptions.UserRequestException;
import by.sorface.passport.web.records.I18Codes;
import by.sorface.passport.web.records.principals.DefaultPrincipal;
import by.sorface.passport.web.records.requests.ApplicationClientPatchRequest;
import by.sorface.passport.web.records.requests.ApplicationRegistry;
import by.sorface.passport.web.records.responses.ApplicationClient;
import by.sorface.passport.web.records.responses.ApplicationClientRefreshSecret;
import by.sorface.passport.web.services.clients.OAuth2ClientService;
import by.sorface.passport.web.utils.HashUtils;
import by.sorface.passport.web.utils.NullUtils;
import by.sorface.passport.web.utils.URLUtils;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultApplicationClientFacade implements ApplicationClientFacade {

    private final OAuth2ClientService oAuth2ClientService;

    private final OAuth2ClientConverter oAuth2ClientConverter;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<ApplicationClient> findByCurrentUser() {
        log.info("Find application clients by current user.");

        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        final var principal = (DefaultPrincipal) authentication.getPrincipal();

        log.debug("Find application clients by current user [id -> {}]", principal.getId());

        final var applicationClients = oAuth2ClientService.findAllByUserId(principal.getId()).stream()
                .map(oAuth2ClientConverter::convertWithoutSecret)
                .collect(Collectors.toList());

        log.info("Find application clients [ids -> {}] by current user [id -> {}]", applicationClients.stream()
                .map(ApplicationClient::getId).collect(Collectors.toList()), principal.getId());

        return applicationClients;
    }

    @Override
    public ApplicationClient getByIdAndCurrentUser(UUID clientId) {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        final var principal = (DefaultPrincipal) authentication.getPrincipal();

        OAuth2Client oAuth2Client = oAuth2ClientService.findByIdAndUserId(clientId, principal.getId());

        if (Objects.isNull(oAuth2Client)) {
            throw new NotFoundException(I18Codes.I18ClientCodes.NOT_FOUND_BY_ID, Map.of("id", clientId.toString()));
        }

        return oAuth2ClientConverter.convertWithoutSecret(oAuth2Client);
    }

    @Override
    public ApplicationClientRefreshSecret refreshSecret(final UUID clientId) {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        final var principal = (DefaultPrincipal) authentication.getPrincipal();

        final var oAuth2Client = oAuth2ClientService.findByIdAndUserId(clientId, principal.getId());

        if (Objects.isNull(oAuth2Client)) {
            throw new NotFoundException(I18Codes.I18ClientCodes.NOT_FOUND_BY_ID, Map.of("id", clientId.toString()));
        }

        final var clientSecret = generateClientSecret();

        oAuth2Client.setClientSecret(passwordEncoder.encode(clientSecret));
        oAuth2Client.setClientSecretExpiresAt(buildDefaultClientSecretExpire());

        oAuth2ClientService.save(oAuth2Client);

        return ApplicationClientRefreshSecret.builder()
                .clientSecret(clientSecret)
                .expiresAt(buildDefaultClientSecretExpire())
                .build();
    }

    @Override
    public void delete(final UUID clientId) {
        if (!oAuth2ClientService.isExists(clientId)) {
            throw new NotFoundException(I18Codes.I18ClientCodes.NOT_FOUND_BY_ID, Map.of("id", clientId.toString()));
        }

        oAuth2ClientService.delete(clientId);
    }

    @Override
    public ApplicationClient registry(final ApplicationRegistry registryClient) {
        final var clientSecret = generateClientSecret();

        final var oAuth2Client = new OAuth2Client();
        {
            oAuth2Client.setClientId(HashUtils.generateRegistryHash(55) + "@sorface.oauth.client");
            oAuth2Client.setClientSecret(passwordEncoder.encode(clientSecret));
            oAuth2Client.setClientName(registryClient.name());
            oAuth2Client.setClientIdIssueAt(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
            oAuth2Client.setClientSecretExpiresAt(buildDefaultClientSecretExpire());
            oAuth2Client.setRedirectUris(String.join(";", registryClient.redirectionUrls()));
        }

        final var oAuth2ClientSaved = oAuth2ClientService.save(oAuth2Client);

        return oAuth2ClientConverter.convert(oAuth2ClientSaved, clientSecret);
    }

    @Override
    @Transactional
    @NewSpan("partial-update")
    @org.springframework.cloud.sleuth.annotation.NewSpan("partial-update")
    public ApplicationClient partialUpdate(final UUID clientId, final ApplicationClientPatchRequest request) {
        log.info("patch update application client [id -> {}]", clientId);

        final OAuth2Client oAuth2Client = oAuth2ClientService.findById(clientId);

        if (Objects.isNull(oAuth2Client)) {
            throw new NotFoundException(I18Codes.I18ClientCodes.NOT_FOUND_BY_ID, Map.of("id", clientId.toString()));
        }

        log.info("found application client with [id -> {}]", clientId);

        NullUtils.setIfNonNull(request.getRedirectionUrls(), redirectUrls -> {
            for (final var url : redirectUrls) {
                if (URLUtils.isValidRedirectUrl(url)) {
                    continue;
                }

                throw new UserRequestException(I18Codes.I18ClientCodes.REDIRECT_URL_NOT_VALID, Map.of("url", url));
            }

            final var redirects = String.join(";", redirectUrls);

            oAuth2Client.setRedirectUris(redirects);
        });

        NullUtils.setIfNonNull(request.getName(), oAuth2Client::setClientName);

        oAuth2ClientService.save(oAuth2Client);

        return oAuth2ClientConverter.convertWithoutSecret(oAuth2Client);
    }

    private LocalDateTime buildDefaultClientSecretExpire() {
        return LocalDateTime.ofInstant(Instant.now().plus(300, ChronoUnit.DAYS), ZoneOffset.UTC);
    }

    private String generateClientSecret() {
        return HashUtils.generateRegistryHash(55);
    }

}
