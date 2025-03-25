package by.sorface.idp.dao.sql.repository.client

import by.sorface.idp.dao.sql.model.client.ClientSettingModel
import by.sorface.idp.dao.sql.model.client.ClientTokenSettingModel
import by.sorface.idp.dao.sql.model.client.RegisteredClientModel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service
class JpaRegisteredClientRepository : RegisteredClientRepository {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var clientAuthenticationGrantTypeRepository: ClientAuthenticationGrantTypeRepository

    @Autowired
    private lateinit var clientAuthenticationMethodRepository: ClientAuthenticationMethodRepository

    @Autowired
    private lateinit var clientScopeRepository: ClientScopeRepository

    @Autowired
    private lateinit var jdbcRegisteredClientRepository: JdbcRegisteredClientRepository

    override fun save(registeredClient: RegisteredClient) {
        val registeredClientModel = RegisteredClientModel().apply {
            id = UUID.fromString(registeredClient.id)

            clientId = registeredClient.clientId
            clientSecret = registeredClient.clientSecret
            clientName = registeredClient.clientName

            redirectUris = registeredClient.redirectUris
                .mapNotNull { redirectUrl -> by.sorface.idp.dao.sql.model.client.ClientRedirectUrlModel().apply { this.url = redirectUrl } }
                .toMutableSet()

            methods = registeredClient.clientAuthenticationMethods
                .mapNotNull { method ->
                    val clientAuthenticationMethodModel = clientAuthenticationMethodRepository.findFirstByMethodEndingWithIgnoreCase(method.value)

                    clientAuthenticationMethodModel ?: RuntimeException("Method [$method] not registered")

                    return@mapNotNull clientAuthenticationMethodModel
                }
                .toMutableSet()

            grantTypes = registeredClient.authorizationGrantTypes
                .mapNotNull { grantType ->
                    val clientAuthenticationGrantTypeModel = clientAuthenticationGrantTypeRepository.findFirstByGrantTypeEndingWithIgnoreCase(grantType.value)

                    clientAuthenticationGrantTypeModel ?: RuntimeException("Grant type [$grantType] not registered")

                    return@mapNotNull clientAuthenticationGrantTypeModel
                }
                .toMutableSet()

            scopes = registeredClient.scopes
                .mapNotNull { scope ->
                    val clientScopeModel = clientScopeRepository.findFirstByScope(scope)

                    clientScopeModel ?: RuntimeException("Scope [$scope] not registered")

                    return@mapNotNull clientScopeModel
                }
                .toMutableSet()

            clientSetting = ClientSettingModel().apply {
                val clientSettings = registeredClient.clientSettings

                requireConcept = clientSettings.isRequireAuthorizationConsent
                requireProofKey = clientSettings.isRequireProofKey
            }

            tokenSetting = ClientTokenSettingModel().apply {
                val tokenSettings = registeredClient.tokenSettings

                accessTokenFormat = tokenSettings.accessTokenFormat.value
                idTokenSignatureAlgorithm = tokenSettings.idTokenSignatureAlgorithm
                accessTokenTimeToLive = tokenSettings.accessTokenTimeToLive.toSeconds()
                refreshTokenTimeToLive = tokenSettings.refreshTokenTimeToLive.toSeconds()
                reuseRefreshTokens = tokenSettings.isReuseRefreshTokens
            }
        }

        jdbcRegisteredClientRepository.save(registeredClientModel)
    }

    override fun findById(id: String): RegisteredClient? {
        logger.info("searching for client by id: {}", id)

        val registeredClient = jdbcRegisteredClientRepository.findById(UUID.fromString(id)).map {
            logger.info("client found by id: {}", id)

            map(it)
        }.orElseGet {
            logger.info("client not fount by id: {}", id)

            null
        }

        return registeredClient
    }

    override fun findByClientId(clientId: String): RegisteredClient? {
        logger.info("searching for clientId with id: {}", clientId)

        val registeredClient = jdbcRegisteredClientRepository.findByClientId(clientId)

        if (registeredClient== null)  {
            logger.info("no client with clientId: {}", clientId)

            return null
        }

        logger.info("found client with clientId: {}", registeredClient)

        return map(registeredClient)
    }

    private fun map(registeredClientModel: RegisteredClientModel): RegisteredClient {
        return RegisteredClient.withId(registeredClientModel.id.toString()).apply {
            clientId(registeredClientModel.clientId)
            clientName(registeredClientModel.clientName)
            clientSecret(registeredClientModel.clientSecret)
            clientIdIssuedAt(registeredClientModel.clientIdIssueAt)
            clientSecretExpiresAt(registeredClientModel.clientSecretExpiresAt)
            val methods = registeredClientModel.methods.map { ClientAuthenticationMethod(it.method) }.toSet()

            clientAuthenticationMethods { it.addAll(methods) }

            val grantTypes = registeredClientModel.grantTypes.map { AuthorizationGrantType(it.grantType) }.toMutableSet()

            authorizationGrantTypes { it.addAll(grantTypes) }

            val redirectUrls = registeredClientModel.redirectUris.mapNotNull { model -> model.url }.toMutableSet()

            redirectUris { it.addAll(redirectUrls) }

            val scopes = registeredClientModel.scopes.map { it.scope }.toMutableSet()

            scopes { it.addAll(scopes) }

            val registeredClientSettings: ClientSettingModel = registeredClientModel.clientSetting!!

            clientSettings(
                ClientSettings.builder()
                    .requireProofKey(registeredClientSettings.requireProofKey)
                    .requireAuthorizationConsent(registeredClientSettings.requireConcept)
                    .build()
            )

            val registeredTokenSettingModel = registeredClientModel.tokenSetting!!

            tokenSettings(
                TokenSettings.builder()
                    .accessTokenFormat(OAuth2TokenFormat(registeredTokenSettingModel.accessTokenFormat))
                    .idTokenSignatureAlgorithm(registeredTokenSettingModel.idTokenSignatureAlgorithm)
                    .accessTokenTimeToLive(Duration.ofSeconds(registeredTokenSettingModel.accessTokenTimeToLive))
                    .refreshTokenTimeToLive(Duration.ofSeconds(registeredTokenSettingModel.refreshTokenTimeToLive))
                    .reuseRefreshTokens(registeredTokenSettingModel.reuseRefreshTokens)
                    .build()
            )
        }.build()
    }

}