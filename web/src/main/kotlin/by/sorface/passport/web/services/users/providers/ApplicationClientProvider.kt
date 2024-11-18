package by.sorface.passport.web.services.users.providers

import by.sorface.passport.web.config.options.ClientTokenOptions
import by.sorface.passport.web.config.options.ClientTokenOptions.TokenSetting
import by.sorface.passport.web.dao.sql.models.OAuth2Client
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.services.clients.OAuth2ClientService
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.stereotype.Service
import java.nio.file.AccessDeniedException
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Service
class ApplicationClientProvider(
    private val oAuth2ClientService: OAuth2ClientService,
    private val clientTokenOptions: ClientTokenOptions
) : RegisteredClientRepository {

    override fun save(registeredClient: RegisteredClient) {
        val oAuth2Client = OAuth2Client()
        run {
            oAuth2Client.id = UUID.fromString(registeredClient.id)
            oAuth2Client.clientId = registeredClient.clientId
            oAuth2Client.clientName = registeredClient.clientName
            oAuth2Client.clientSecret = registeredClient.clientSecret

            val clientIdIssueAt = if (registeredClient.clientIdIssuedAt != null
            ) LocalDateTime.ofInstant(registeredClient.clientIdIssuedAt, ZoneOffset.UTC)
            else null

            oAuth2Client.clientIdIssueAt = clientIdIssueAt

            val clientSecretExpiresAt = if (registeredClient.clientSecretExpiresAt != null
            ) LocalDateTime.ofInstant(registeredClient.clientSecretExpiresAt, ZoneOffset.UTC)
            else null

            oAuth2Client.clientSecretExpiresAt = clientSecretExpiresAt
            oAuth2Client.redirectUris = java.lang.String.join(REDIRECT_URL_SPLITERATOR, registeredClient.redirectUris)
        }

        oAuth2ClientService.save(oAuth2Client)
    }

    override fun findById(id: String): RegisteredClient {
        val oAuth2Client = oAuth2ClientService.findById(UUID.fromString(id)) ?: throw AccessDeniedException(I18Codes.I18ClientCodes.NOT_FOUND)

        return this.buildClient(oAuth2Client)
    }

    override fun findByClientId(clientId: String): RegisteredClient {
        val oAuth2Client = oAuth2ClientService.findByClientId(clientId) ?: throw AccessDeniedException(I18Codes.I18ClientCodes.NOT_FOUND)

        return buildClient(oAuth2Client)
    }

    private fun buildClient(oAuth2Client: OAuth2Client): RegisteredClient {
        val redirectUrls = this.getRedirectUrls(oAuth2Client.redirectUris)

        val scopes = setOf("scope.read", "scope.write", OidcScopes.OPENID, OidcScopes.PROFILE, OidcScopes.EMAIL)

        val accessTokenSettings: TokenSetting = clientTokenOptions.accessToken
        val refreshTokenSettings: TokenSetting = clientTokenOptions.refreshToken
        val authorizationCodeSettings: TokenSetting = clientTokenOptions.authorizationCode

        val accessTokenTimeToLive = Duration.of(accessTokenSettings.timeToLiveValue, accessTokenSettings.timeToLiveCron)
        val refreshTokenTimeToLive = Duration.of(refreshTokenSettings.timeToLiveValue, refreshTokenSettings.timeToLiveCron)

        val tokenSettings = TokenSettings.builder()
            .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
            .accessTokenTimeToLive(accessTokenTimeToLive)
            .refreshTokenTimeToLive(refreshTokenTimeToLive)
            .reuseRefreshTokens(true)
            .deviceCodeTimeToLive(accessTokenTimeToLive)
            .authorizationCodeTimeToLive(Duration.of(authorizationCodeSettings.timeToLiveValue, authorizationCodeSettings.timeToLiveCron))
            .build()

        val grantTypes = setOf(
            AuthorizationGrantType.AUTHORIZATION_CODE,
            AuthorizationGrantType.CLIENT_CREDENTIALS,
            AuthorizationGrantType.REFRESH_TOKEN
        )

        val clientsAuthMethod = listOf(
            ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
            ClientAuthenticationMethod.CLIENT_SECRET_POST
        )

        val registeredClientBuilder = RegisteredClient
            .withId(oAuth2Client.id.toString())
            .clientId(oAuth2Client.clientId)
            .clientSecret(oAuth2Client.clientSecret)
            .clientIdIssuedAt(oAuth2Client.clientIdIssueAt?.toInstant(ZoneOffset.UTC))
            .clientSecretExpiresAt(oAuth2Client.clientSecretExpiresAt?.toInstant(ZoneOffset.UTC))
            .clientName(oAuth2Client.clientName)
            .clientAuthenticationMethods { clientAuthenticationMethods: MutableSet<ClientAuthenticationMethod> -> clientAuthenticationMethods.addAll(clientsAuthMethod) }
            .authorizationGrantTypes { authorizationGrantTypes: MutableSet<AuthorizationGrantType> -> authorizationGrantTypes.addAll(grantTypes) }
            .redirectUris { redirectUris: MutableSet<String> -> redirectUris.addAll(redirectUrls) }
            .scopes { scopeFunc: MutableSet<String> -> scopeFunc.addAll(scopes) }
            .tokenSettings(tokenSettings)

        oAuth2Client.postLogoutRedirectUri?.let {
            registeredClientBuilder.postLogoutRedirectUri(it)
        }

        return registeredClientBuilder.build()
    }

    private fun getRedirectUrls(value: String?): List<String> {
        value ?: return emptyList()

        return Arrays.stream(value.split(REDIRECT_URL_SPLITERATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()).toList()
    }

    companion object {
        const val REDIRECT_URL_SPLITERATOR: String = ";"
    }
}
