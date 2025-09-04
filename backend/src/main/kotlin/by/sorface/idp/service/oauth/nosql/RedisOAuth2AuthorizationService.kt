package by.sorface.idp.service.oauth.nosql

import by.sorface.idp.dao.nosql.model.OAuth2AuthorizationComplete
import by.sorface.idp.dao.nosql.model.OAuth2AuthorizationInit
import by.sorface.idp.dao.nosql.repository.RedisOAuth2AuthorizationCompleteRepository
import by.sorface.idp.dao.nosql.repository.RedisOAuth2AuthorizationInitRepository
import by.sorface.idp.extencions.hasIdHash
import by.sorface.idp.records.events.SessionRefreshEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.lang.Nullable
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.session.data.redis.RedisIndexedSessionRepository
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

/**
 * Служба авторизации OAuth2, использующая Redis для хранения данных.
 * Реализует интерфейс [OAuth2AuthorizationService] и управляет состоянием авторизации,
 * сохраняя начальные и завершённые объекты авторизации в соответствующие репозитории.
 */
@Service
class RedisOAuth2AuthorizationService(
    /**
     * Репозиторий для хранения полностью сформированных объектов авторизации.
     */
    private val redisOAuth2AuthorizationCompleteRepository: RedisOAuth2AuthorizationCompleteRepository,

    /**
     * Репозиторий для хранения начальных объектов авторизации.
     */
    private val redisOAuth2AuthorizationInitRepository: RedisOAuth2AuthorizationInitRepository,

    private val applicationEventPublisher: ApplicationEventPublisher,
    private val redisIndexRedisIndexedSessionRepository: RedisIndexedSessionRepository
) : OAuth2AuthorizationService {

    /**
     * Логгер для регистрации действий сервиса.
     */
    private val logger = LoggerFactory.getLogger(RedisOAuth2AuthorizationService::class.java)

    /**
     * Сохраняет объект авторизации в репозитории Redis.
     * Если авторизация завершена — сохраняет в [redisOAuth2AuthorizationCompleteRepository],
     * иначе — в [redisOAuth2AuthorizationInitRepository].
     *
     * @param authorization объект авторизации
     */
    override fun save(authorization: OAuth2Authorization) {
        Assert.notNull(authorization, "authorization cannot be null")

        logger.info("save user's authorization object with id ${authorization.id}")

        if (authorization.isComplete()) {
            if (java.lang.Boolean.TRUE == redisOAuth2AuthorizationInitRepository.existsById(authorization.id)) {
                redisOAuth2AuthorizationInitRepository.deleteById(authorization.id)
            }

            logger.info("saved user's complete authorization object with id ${authorization.id}")

            redisOAuth2AuthorizationCompleteRepository.save(authorization.toComplete())

            this.publishSessionRefreshEvent(authorization)
        } else {
            logger.info("saved user's init authorization object with id ${authorization.id}")

            redisOAuth2AuthorizationInitRepository.save(authorization.toInit())
        }
    }

    /**
     * Удаляет объект авторизации из репозиториев.
     *
     * @param authorization объект авторизации
     */
    override fun remove(authorization: OAuth2Authorization) {
        Assert.notNull(authorization, "authorization cannot be null")

        logger.debug("remove authorization object with key ${authorization.id}")

        redisOAuth2AuthorizationCompleteRepository.deleteById(authorization.id)
        redisOAuth2AuthorizationInitRepository.deleteById(authorization.id)
    }

    /**
     * Находит объект авторизации по его идентификатору.
     *
     * @param id идентификатор авторизации
     * @return найденный объект авторизации или null
     */
    @Nullable
    override fun findById(id: String): OAuth2Authorization? =
        redisOAuth2AuthorizationCompleteRepository.findByIdOrNull(id)?.authorization
            ?: redisOAuth2AuthorizationInitRepository.findByIdOrNull(id)?.authorization

    /**
     * Находит объект авторизации по токену и типу токена.
     *
     * @param token значение токена
     * @param tokenType тип токена
     * @return найденный объект авторизации или null
     */
    override fun findByToken(token: String, @Nullable tokenType: OAuth2TokenType?): OAuth2Authorization? {
        Assert.hasText(token, "token cannot be empty")

        logger.info("find authorization by token type $tokenType")

        tokenType ?: return redisOAuth2AuthorizationInitRepository.findFirstByCode(token)?.authorization
            ?: redisOAuth2AuthorizationInitRepository.findFirstByState(token)?.authorization
            ?: redisOAuth2AuthorizationCompleteRepository.findFirstByAccessToken(token)?.authorization
            ?: redisOAuth2AuthorizationCompleteRepository.findFirstByRefreshToken(token)?.authorization
            ?: redisOAuth2AuthorizationCompleteRepository.findFirstByOidcToken(token)?.authorization

        val oAuth2Authorization = when (tokenType.value) {
            OAuth2ParameterNames.CODE -> {
                logger.info("use search strategy [OAuth2ParameterNames.CODE] for authorization by token type ${tokenType.value}")
                redisOAuth2AuthorizationInitRepository.findFirstByCode(token)?.authorization
            }

            OAuth2ParameterNames.STATE -> {
                logger.info("use search strategy [OAuth2ParameterNames.STATE] for authorization by token type ${tokenType.value}")

                redisOAuth2AuthorizationInitRepository.findFirstByState(token)?.authorization
            }

            OAuth2ParameterNames.ACCESS_TOKEN -> {
                logger.info("use search strategy [OAuth2ParameterNames.ACCESS_TOKEN] for authorization by token type ${tokenType.value}")

                redisOAuth2AuthorizationCompleteRepository.findFirstByAccessToken(token)?.authorization
            }

            OAuth2ParameterNames.REFRESH_TOKEN -> {
                logger.info("use search strategy [OAuth2ParameterNames.REFRESH_TOKEN] for authorization by token type ${tokenType.value}")

                redisOAuth2AuthorizationCompleteRepository.findFirstByRefreshToken(token)?.authorization
            }

            OidcParameterNames.ID_TOKEN -> {
                logger.info("use search strategy [OidcParameterNames.ID_TOKEN] for authorization by token type ${tokenType.value}")

                redisOAuth2AuthorizationCompleteRepository.findFirstByOidcToken(token)?.authorization
            }

            else -> {
                logger.info("use search strategy [OAuth2ParameterNames.OIDC_TOKEN] for authorization by token type ${tokenType.value}")

                redisOAuth2AuthorizationCompleteRepository.findFirstByOidcToken(token)?.authorization
            }
        }

        if (oAuth2Authorization != null) {
            logger.info("found authorization object with id ${oAuth2Authorization.id}")
        } else {
            logger.info("not found authorization object by token type ${tokenType.value}")
        }

        return oAuth2Authorization
    }

    /**
     * Проверяет, является ли авторизация завершённой.
     *
     * @return true, если есть access token
     */
    private fun OAuth2Authorization.isComplete(): Boolean = this.accessToken != null

    /**
     * Преобразует объект авторизации в полностью сформированный объект.
     *
     * @return объект [OAuth2AuthorizationComplete]
     */
    private fun OAuth2Authorization.toComplete(): OAuth2AuthorizationComplete = OAuth2AuthorizationComplete()
        .also { completeAuthorization ->
            val oidcToken = this.getToken(OidcIdToken::class.java)
            val oidcTokenValue = oidcToken.getTokenValueOrNull()

            logger.info("convert authorization object to complete. save authorization [id -> ${this.id}, claims oidc token ->  ${oidcToken?.claims}]")

            completeAuthorization.id = this.id
            completeAuthorization.principalName = this.principalName
            completeAuthorization.accessToken = this.accessToken.getTokenValueOrNull()
            completeAuthorization.refreshToken = this.refreshToken.getTokenValueOrNull()
            completeAuthorization.oidcToken = oidcTokenValue
            completeAuthorization.authorization = this
        }

    /**
     * Преобразует объект авторизации в начальный объект.
     *
     * @return объект [OAuth2AuthorizationInit]
     */
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

    /**
     * Получает значение токена из объекта токена.
     *
     * @return значение токена или null
     */
    private fun <T : OAuth2Token> OAuth2Authorization.Token<T>?.getTokenValueOrNull(): String? {
        return this?.token?.tokenValue
    }

    /**
     * Публикует событие обновления сессии на основе авторизации.
     * Извлекает session ID из OIDC ID токена и публикует SessionRefreshEvent.
     *
     * @param authorization объект авторизации OAuth2
     */
    private fun publishSessionRefreshEvent(authorization: OAuth2Authorization) {
        val sessionIdHash = authorization.getSessionId() ?: return

        val sessions = redisIndexRedisIndexedSessionRepository.findByPrincipalName(authorization.principalName!!)

        sessions.values
            .filter { session -> session.hasIdHash(sessionIdHash) }
            .forEach { session ->
                logger.debug("publishing session refresh event for session [id -> {}]", session.id)

                applicationEventPublisher.publishEvent(SessionRefreshEvent(session.id))
            }
    }

    /**
     * Получает session ID (sid) из claims OIDC ID токена.
     *
     * @return session ID или null, если не найден
     */
    private fun OAuth2Authorization.getSessionId(): String? {
        val idToken = this.getToken(OidcIdToken::class.java)?.token

        return idToken?.claims?.get("sid") as? String
    }

}
