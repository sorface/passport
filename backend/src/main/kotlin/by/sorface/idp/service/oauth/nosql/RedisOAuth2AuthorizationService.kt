package by.sorface.idp.service.oauth.nosql

import by.sorface.idp.dao.nosql.model.OAuth2AuthorizationComplete
import by.sorface.idp.dao.nosql.model.OAuth2AuthorizationInit
import by.sorface.idp.dao.nosql.repository.RedisOAuth2AuthorizationCompleteRepository
import by.sorface.idp.dao.nosql.repository.RedisOAuth2AuthorizationInitRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.lang.Nullable
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.stereotype.Service
import org.springframework.util.Assert

@Service
class RedisOAuth2AuthorizationService(
    private val redisOAuth2AuthorizationCompleteRepository: RedisOAuth2AuthorizationCompleteRepository,
    private val redisOAuth2AuthorizationInitRepository: RedisOAuth2AuthorizationInitRepository
) : OAuth2AuthorizationService {

    private val logger = LoggerFactory.getLogger(RedisOAuth2AuthorizationService::class.java)

    override fun save(authorization: OAuth2Authorization) {
        Assert.notNull(authorization, "authorization cannot be null")

        logger.info("save user's authorization object with id ${authorization.id}")

        if (authorization.isComplete()) {
            if (java.lang.Boolean.TRUE == redisOAuth2AuthorizationInitRepository.existsById(authorization.id)) {
                redisOAuth2AuthorizationInitRepository.deleteById(authorization.id)
            }

            logger.info("saved user's complete authorization object with id ${authorization.id}")

            redisOAuth2AuthorizationCompleteRepository.save(authorization.toComplete())
        } else {
            logger.info("saved user's init authorization object with id ${authorization.id}")

            redisOAuth2AuthorizationInitRepository.save(authorization.toInit())
        }
    }

    override fun remove(authorization: OAuth2Authorization) {
        Assert.notNull(authorization, "authorization cannot be null")

        logger.debug("remove authorization object with key ${authorization.id}")

        redisOAuth2AuthorizationCompleteRepository.deleteById(authorization.id)
        redisOAuth2AuthorizationInitRepository.deleteById(authorization.id)
    }

    @Nullable
    override fun findById(id: String): OAuth2Authorization? =
        redisOAuth2AuthorizationCompleteRepository.findByIdOrNull(id)?.authorization
            ?: redisOAuth2AuthorizationInitRepository.findByIdOrNull(id)?.authorization

    override fun findByToken(token: String, @Nullable tokenType: OAuth2TokenType?): OAuth2Authorization? {
        Assert.hasText(token, "token cannot be empty")

        tokenType ?: return redisOAuth2AuthorizationInitRepository.findFirstByCode(token)?.authorization
            ?: redisOAuth2AuthorizationInitRepository.findFirstByState(token)?.authorization
            ?: redisOAuth2AuthorizationCompleteRepository.findFirstByAccessToken(token)?.authorization
            ?: redisOAuth2AuthorizationCompleteRepository.findFirstByRefreshToken(token)?.authorization
            ?: redisOAuth2AuthorizationCompleteRepository.findFirstByOidcToken(token)?.authorization

        return when (tokenType.value) {
            OAuth2ParameterNames.CODE -> {
                redisOAuth2AuthorizationInitRepository.findFirstByCode(token)?.authorization
            }

            OAuth2ParameterNames.STATE -> {
                redisOAuth2AuthorizationInitRepository.findFirstByState(token)?.authorization
            }

            OAuth2ParameterNames.ACCESS_TOKEN -> {
                redisOAuth2AuthorizationCompleteRepository.findFirstByAccessToken(token)?.authorization
            }

            OAuth2ParameterNames.REFRESH_TOKEN -> {
                redisOAuth2AuthorizationCompleteRepository.findFirstByRefreshToken(token)?.authorization
            }

            else -> {
                redisOAuth2AuthorizationCompleteRepository.findFirstByOidcToken(token)?.authorization
            }
        }
    }

    private fun OAuth2Authorization.isComplete(): Boolean = this.accessToken != null

    private fun OAuth2Authorization.toComplete(): OAuth2AuthorizationComplete = OAuth2AuthorizationComplete()
        .also {
            it.id = this.id
            it.accessToken = this.accessToken.getTokenValueOrNull()
            it.refreshToken = this.refreshToken.getTokenValueOrNull()
            it.oidcToken = this.getToken(OidcIdToken::class.java).getTokenValueOrNull()
            it.authorization = this
        }

    private fun OAuth2Authorization.toInit(): OAuth2AuthorizationInit = OAuth2AuthorizationInit()
        .also { initAuthorization ->
            val state = this.getAttribute<Any>(OAuth2ParameterNames.STATE)
                ?.takeIf { attribute -> attribute is String }
                ?.toString()

            initAuthorization.id = this.id
            initAuthorization.code = this.getToken(OAuth2AuthorizationCode::class.java).getTokenValueOrNull()
            initAuthorization.state = state
            initAuthorization.authorization = this
        }

    private fun <T : OAuth2Token> OAuth2Authorization.Token<T>?.getTokenValueOrNull(): String? {
        return this?.token?.tokenValue
    }

}
