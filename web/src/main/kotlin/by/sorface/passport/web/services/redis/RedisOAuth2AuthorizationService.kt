package by.sorface.passport.web.services.redis

import by.sorface.passport.web.dao.nosql.redis.RedisOAuth2AuthorizationCompleteRepository
import by.sorface.passport.web.dao.nosql.redis.RedisOAuth2AuthorizationInitRepository
import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2AuthorizationComplete
import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2AuthorizationInit
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
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
import java.util.function.Function

@Slf4j
@Service
@RequiredArgsConstructor
class RedisOAuth2AuthorizationService(
    private val redisOAuth2AuthorizationCompleteRepository: RedisOAuth2AuthorizationCompleteRepository,
    private val redisOAuth2AuthorizationInitRepository: RedisOAuth2AuthorizationInitRepository
) : OAuth2AuthorizationService {

    override fun save(authorization: OAuth2Authorization) {
        Assert.notNull(authorization, "authorization cannot be null")

        logger.info("save user's authorization object with id ${authorization.id}")

        if (isComplete(authorization)) {
            if (java.lang.Boolean.TRUE == redisOAuth2AuthorizationInitRepository.existsById(authorization.id)) {
                redisOAuth2AuthorizationInitRepository.deleteById(authorization.id)
            }

            val authorizationComplete = RedisOAuth2AuthorizationComplete.builder()
                .id(authorization.id)
                .accessToken(getTokenValueOrNull(authorization.accessToken))
                .refreshToken(getTokenValueOrNull(authorization.refreshToken))
                .oidcToken(getTokenValueOrNull(authorization.getToken(OidcIdToken::class.java)))
                .authorization(authorization)
                .build()

            logger.info("saved user's complete authorization object with id ${authorization.id}")

            redisOAuth2AuthorizationCompleteRepository.save(authorizationComplete)
        } else {
            val state = Optional.ofNullable(authorization.getAttribute<Any>(OAuth2ParameterNames.STATE))
                .filter { obj: Any? -> String::class.java.isInstance(obj) }
                .map { obj: Any? -> java.lang.String.valueOf(obj) }
                .orElse(null)

            val code = getTokenValueOrNull(authorization.getToken(OAuth2AuthorizationCode::class.java))

            val authorizationInit = RedisOAuth2AuthorizationInit.builder()
                .id(authorization.id)
                .code(code)
                .state(state)
                .authorization(authorization)
                .build()

            logger.info("saved user's init authorization object with id ${authorization.id}")

            redisOAuth2AuthorizationInitRepository.save(authorizationInit)
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
            .map { obj: RedisOAuth2AuthorizationComplete -> obj.authorization }
            .or { redisOAuth2AuthorizationInitRepository.findById(id).map { it?.authorization } }
            .orElse(null)
    }

    override fun findByToken(token: String, @Nullable tokenType: OAuth2TokenType?): OAuth2Authorization? {
        Assert.hasText(token, "token cannot be empty")

        if (Objects.isNull(tokenType)) {
            return redisOAuth2AuthorizationInitRepository.findFirstByCode(token).map { it.authorization }.orElse(null)
                ?: redisOAuth2AuthorizationInitRepository.findFirstByState(token).map { it.authorization }.orElse(null)
                ?: redisOAuth2AuthorizationCompleteRepository.findFirstByAccessToken(token).map { it.authorization }.orElse(null)
                ?: redisOAuth2AuthorizationCompleteRepository.findFirstByRefreshToken(token).map { it.authorization }.orElse(null)
                ?: redisOAuth2AuthorizationCompleteRepository.findFirstByOidcToken(token).map { it.authorization }.orElse(null)
        }

        return when (tokenType?.value) {
            OAuth2ParameterNames.CODE -> redisOAuth2AuthorizationInitRepository.findFirstByCode(token)
                .map { it.authorization }
                .orElse(null)

            OAuth2ParameterNames.STATE -> redisOAuth2AuthorizationInitRepository.findFirstByState(token)
                .map { it.authorization }
                .orElse(null)

            OAuth2ParameterNames.ACCESS_TOKEN -> redisOAuth2AuthorizationCompleteRepository.findFirstByAccessToken(token)
                .map { it.authorization }
                .orElse(null)

            OAuth2ParameterNames.REFRESH_TOKEN -> return redisOAuth2AuthorizationCompleteRepository.findFirstByRefreshToken(token)
                .map { it.authorization }
                .orElse(null)

            else -> redisOAuth2AuthorizationCompleteRepository.findFirstByOidcToken(token)
                .map { it.authorization }
                .orElse(null)
        }
    }

    private fun <T : OAuth2Token> getTokenValueOrNull(oauth2Token: OAuth2Authorization.Token<T>?): String? {
        return Optional.ofNullable<OAuth2Authorization.Token<T>>(oauth2Token)
            .map { it.token }
            .filter { Objects.nonNull(it) }
            .map { it.tokenValue }
            .orElse(null)
    }

    companion object {
        private fun isComplete(authorization: OAuth2Authorization): Boolean {
            return authorization.accessToken != null
        }

        private val logger = LoggerFactory.getLogger(RedisOAuth2AuthorizationService::class.java)
    }

}
