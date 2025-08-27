package by.sorface.idp.config.security.session.impl

import by.sorface.idp.config.security.backchannel.context.OidcLogoutContext
import by.sorface.idp.config.security.backchannel.dispatcher.BackchannelEventDispatcher
import by.sorface.idp.config.security.backchannel.dispatcher.BackchannelLogoutEvent
import by.sorface.idp.config.security.backchannel.generator.JwtLogoutGenerator
import by.sorface.idp.config.security.session.SessionManager
import by.sorface.idp.dao.nosql.model.OAuth2AuthorizationComplete
import by.sorface.idp.dao.nosql.repository.RedisOAuth2AuthorizationCompleteRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session
import org.springframework.session.data.redis.RedisIndexedSessionRepository
import org.springframework.stereotype.Component

/**
 * Реализация менеджера сессий.
 *
 * @param redisIndexedSessionRepository Репозиторий сессий Redis.
 * @param findByIndexNameSessionRepository Репозиторий сессий, который позволяет искать сессии по имени индекса.
 * @param redisOAuth2AuthorizationCompleteRepository Репозиторий для работы с OAuth2 авторизациями.
 * @param backchannelEventDispatcher Диспетчер событий обратного канала.
 * @param jwtLogoutGenerator Генератор JWT для логаута.
 * @param registeredClientRepository Репозиторий зарегистрированных клиентов.
 */
@Component
class DefaultSessionManager(
    private val redisIndexedSessionRepository: RedisIndexedSessionRepository,
    private val findByIndexNameSessionRepository: FindByIndexNameSessionRepository<out Session>,
    private val redisOAuth2AuthorizationCompleteRepository: RedisOAuth2AuthorizationCompleteRepository,
    private val backchannelEventDispatcher: BackchannelEventDispatcher,
    private val jwtLogoutGenerator: JwtLogoutGenerator,
    private val registeredClientRepository: RegisteredClientRepository,
) : SessionManager {

    /**
     * Сбрасывает сессию по идентификатору и имени пользователя.
     *
     * @param sessionId Идентификатор сессии.
     * @param principalName Имя пользователя.
     */
    override fun resetWithNotify(sessionId: String, principalName: String) {
        reset(sessionId, principalName) { authorization, argSessionId, argPrincipalName ->
            this.dispatchLogoutEvent(authorization, argPrincipalName, argSessionId)
        }
    }

    override fun reset(sessionId: String, principalName: String) {
        reset(sessionId, principalName) { authorization, _, _ -> authorization.id }
    }

    private fun reset(sessionId: String, principalName: String,
                      doAuthorization: (authorization: OAuth2AuthorizationComplete, sessionId: String, principalName: String) -> String) {
        val oauth2Sessions = redisOAuth2AuthorizationCompleteRepository.findAllByPrincipalName(principalName)
            .map { authorization -> doAuthorization(authorization, sessionId, principalName) }

        if (oauth2Sessions.isNotEmpty()) {
            redisOAuth2AuthorizationCompleteRepository.deleteAllById(oauth2Sessions)
        }

        for (redisSession in findByIndexNameSessionRepository.findByPrincipalName(principalName)) {
            redisIndexedSessionRepository.deleteById(redisSession.value?.id)
        }
    }

    /**
     * Отправляет событие логаута всем клиентам OIDC
     *
     * @param authorization Завершенная OAuth2 авторизация.
     * @param principalName Имя пользователя.
     * @param sessionId Идентификатор сессии.
     * @return Идентификатор авторизации.
     */
    private fun dispatchLogoutEvent(authorization: OAuth2AuthorizationComplete, principalName: String, sessionId: String): String {
        val oidcToken = authorization.oidcToken
            ?: return authorization.id

        val oAuth2Authorization = authorization.authorization
            ?: return authorization.id

        val registeredClient = registeredClientRepository.findById(oAuth2Authorization.registeredClientId)
            ?: return authorization.id

        val oidcLogoutContext = OidcLogoutContext.Builder()
            .oidcIdToken(oidcToken)
            .registeredClient(registeredClient)
            .sessionId(sessionId)
            .principalName(principalName)
            .build()

        val jwt = jwtLogoutGenerator.generate(oidcLogoutContext)

        backchannelEventDispatcher.dispatch(BackchannelLogoutEvent(jwt, registeredClient))

        return authorization.id
    }

}