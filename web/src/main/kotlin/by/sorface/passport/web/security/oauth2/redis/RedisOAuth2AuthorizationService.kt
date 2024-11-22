package by.sorface.passport.web.security.oauth2.redis

import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2AuthorizationComplete
import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2AuthorizationInit
import by.sorface.passport.web.dao.nosql.redis.repository.RedisOAuth2AuthorizationCompleteRepository
import by.sorface.passport.web.dao.nosql.redis.repository.RedisOAuth2AuthorizationInitRepository
import org.slf4j.LoggerFactory
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
import java.util.*

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

        logger.debug("Remove authorization object with key ${authorization.id}")

        redisOAuth2AuthorizationCompleteRepository.deleteById(authorization.id)
        redisOAuth2AuthorizationInitRepository.deleteById(authorization.id)
    }

    @Nullable
    override fun findById(id: String): OAuth2Authorization? {
        Assert.hasText(id, "id cannot be empty")

        return redisOAuth2AuthorizationCompleteRepository.findById(id)
            .map { it.authorization }
            .or { redisOAuth2AuthorizationInitRepository.findById(id).map { it?.authorization } }
            .orElse(null)
    }

    override fun findByToken(token: String, @Nullable tokenType: OAuth2TokenType?): OAuth2Authorization? {
        Assert.hasText(token, "token cannot be empty")

        if (tokenType == null) {
            return redisOAuth2AuthorizationInitRepository.findFirstByCode(token)?.authorization
                ?: redisOAuth2AuthorizationInitRepository.findFirstByState(token)?.authorization
                ?: redisOAuth2AuthorizationCompleteRepository.findFirstByAccessToken(token)?.authorization
                ?: redisOAuth2AuthorizationCompleteRepository.findFirstByRefreshToken(token)?.authorization
                ?: redisOAuth2AuthorizationCompleteRepository.findFirstByOidcToken(token)?.authorization
        }

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
                return redisOAuth2AuthorizationCompleteRepository.findFirstByRefreshToken(token)?.authorization
            }

            else -> {
                redisOAuth2AuthorizationCompleteRepository.findFirstByOidcToken(token)?.authorization
            }
        }
    }

    private fun OAuth2Authorization.isComplete(): Boolean = this.accessToken != null

    private fun OAuth2Authorization.toComplete(): RedisOAuth2AuthorizationComplete {
        val authorizationComplete = RedisOAuth2AuthorizationComplete()

        authorizationComplete.id = this.id
        authorizationComplete.accessToken = this.accessToken.getTokenValueOrNull()
        authorizationComplete.refreshToken = this.refreshToken.getTokenValueOrNull()
        authorizationComplete.oidcToken = this.getToken(OidcIdToken::class.java).getTokenValueOrNull()
        authorizationComplete.authorization = this

        return authorizationComplete
    }

    private fun OAuth2Authorization.toInit(): RedisOAuth2AuthorizationInit {
        val state = Optional.ofNullable(this.getAttribute<Any>(OAuth2ParameterNames.STATE))
            .filter { obj: Any? -> String::class.java.isInstance(obj) }
            .map { obj: Any? -> java.lang.String.valueOf(obj) }
            .orElse(null)

        val initAuthorization = RedisOAuth2AuthorizationInit()

        initAuthorization.id = this.id
        initAuthorization.code = this.getToken(OAuth2AuthorizationCode::class.java).getTokenValueOrNull()
        initAuthorization.state = state
        initAuthorization.authorization = this

        return initAuthorization
    }

    private fun <T : OAuth2Token> OAuth2Authorization.Token<T>?.getTokenValueOrNull(): String? {
        return this?.token?.tokenValue
    }

}
