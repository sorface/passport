package by.sorface.idp.config.security.oauth2

import by.sorface.idp.config.security.backchannel.context.OidcLogoutContext
import by.sorface.idp.config.security.backchannel.dispatcher.BackchannelEventDispatcher
import by.sorface.idp.config.security.backchannel.dispatcher.BackchannelLogoutEvent
import by.sorface.idp.config.security.backchannel.generator.JwtLogoutGenerator
import by.sorface.idp.dao.nosql.repository.RedisOAuth2AuthorizationCompleteRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session
import org.springframework.session.data.redis.RedisIndexedSessionRepository
import org.springframework.stereotype.Component

@Component
class CustomLogoutHandler : LogoutHandler {

    private val logger = LoggerFactory.getLogger(CustomLogoutHandler::class.java)

    @Autowired
    private lateinit var jwtLogoutGenerator: JwtLogoutGenerator

    @Autowired
    private lateinit var backchannelEventDispatcher: BackchannelEventDispatcher

    @Autowired
    private lateinit var redisOAuth2AuthorizationCompleteRepository: RedisOAuth2AuthorizationCompleteRepository

    @Autowired
    private lateinit var redisIndexedSessionRepository: RedisIndexedSessionRepository

    @Autowired
    private lateinit var findByIndexNameSessionRepository: FindByIndexNameSessionRepository<out Session>

    override fun logout(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication?) {
        if (authentication == null) return

        val authSessions = redisOAuth2AuthorizationCompleteRepository.findAllByPrincipalName(authentication.name)
            .map {
                val oidcToken = it.oidcToken ?: return@map it.id

                val registeredClient = RegisteredClient.withId("element").build()

                val oidcLogoutContext = OidcLogoutContext.Builder()
                    .oidcIdToken(oidcToken)
                    .registeredClient(registeredClient)
                    .sessionId(it.id)
                    .build()

                val jwt = jwtLogoutGenerator.generate(oidcLogoutContext)

                backchannelEventDispatcher.dispatch(BackchannelLogoutEvent(jwt, registeredClient))

                return@map it.id
            }

        if (authSessions.isNotEmpty()) {
            redisOAuth2AuthorizationCompleteRepository.deleteAllById(authSessions)
        }

        findByIndexNameSessionRepository.findByPrincipalName(authentication.name)
            .map { it.key }
            .forEach { redisIndexedSessionRepository.deleteById(it) }
    }

}