package by.sorface.passport.web.security.oauth2.services

import by.sorface.passport.web.config.options.OAuthTokenProperties
import by.sorface.passport.web.dao.sql.models.OAuth2Client
import by.sorface.passport.web.records.I18Codes
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
    private val oAuthTokenProperties: OAuthTokenProperties
) : RegisteredClientRepository {

    override fun save(registeredClient: RegisteredClient) {
        val oAuth2Client = OAuth2Client().apply {
            id = UUID.fromString(registeredClient.id)
            clientId = registeredClient.clientId
            clientName = registeredClient.clientName
            clientSecret = registeredClient.clientSecret

            clientIdIssueAt = if (registeredClient.clientIdIssuedAt != null)
                LocalDateTime.ofInstant(registeredClient.clientIdIssuedAt, ZoneOffset.UTC)
            else
                null

            clientSecretExpiresAt = if (registeredClient.clientSecretExpiresAt != null)
                LocalDateTime.ofInstant(registeredClient.clientSecretExpiresAt, ZoneOffset.UTC)
            else
                null

            redirectUris = java.lang.String.join(REDIRECT_URL_SPLITERATOR, registeredClient.redirectUris)
        }

        oAuth2ClientService.save(oAuth2Client)
    }

    override fun findById(id: String): RegisteredClient {
        val oAuth2Client = oAuth2ClientService.findById(UUID.fromString(id)) ?: throw AccessDeniedException(I18Codes.I18ClientCodes.NOT_FOUND)
        return this.buildClient(oAuth2Client)
    }

    override fun findByClientId(clientId: String): RegisteredClient? {
        val oAuth2Client = oAuth2ClientService.findByClientId(clientId) ?: throw AccessDeniedException(I18Codes.I18ClientCodes.NOT_FOUND)
        return buildClient(oAuth2Client)
    }

    private fun buildClient(oAuth2Client: OAuth2Client): RegisteredClient {
        val redirectUrls = this.getRedirectUrls(oAuth2Client.redirectUris)

        val scopes = setOf(OidcScopes.OPENID, OidcScopes.PROFILE, OidcScopes.EMAIL)

        val accessTokenTimeToLive = oAuthTokenProperties.access.run { Duration.of(this.ttl, this.cron) }
        val refreshTokenTimeToLive = oAuthTokenProperties.refresh.run { Duration.of(this.ttl, this.cron) }

        val tokenSettings = TokenSettings.builder()
            .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
            .accessTokenTimeToLive(accessTokenTimeToLive)
            .refreshTokenTimeToLive(refreshTokenTimeToLive)
            .reuseRefreshTokens(true)
            .deviceCodeTimeToLive(accessTokenTimeToLive)
            .authorizationCodeTimeToLive(oAuthTokenProperties.authorizationCode.run { Duration.of(this.ttl, this.cron) })
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

        oAuth2Client.postLogoutRedirectUri?.let { registeredClientBuilder.postLogoutRedirectUri(it) }

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
