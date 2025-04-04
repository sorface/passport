package by.sorface.idp.config.security.session

import by.sorface.idp.config.security.backchannel.context.OidcLogoutContext
import by.sorface.idp.config.security.backchannel.dispatcher.BackchannelEventDispatcher
import by.sorface.idp.config.security.backchannel.dispatcher.BackchannelLogoutEvent
import by.sorface.idp.config.security.backchannel.dispatcher.RestBackchannelEventDispatcher
import by.sorface.idp.config.security.backchannel.generator.JwtLogoutGenerator
import by.sorface.idp.dao.nosql.repository.RedisOAuth2AuthorizationCompleteRepository
import by.sorface.idp.extencions.getPrincipalUsername
import org.apache.tomcat.util.http.parser.Authorization
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session
import org.springframework.session.data.redis.RedisIndexedSessionRepository
import org.springframework.session.events.SessionCreatedEvent
import org.springframework.session.events.SessionDeletedEvent
import org.springframework.session.events.SessionExpiredEvent
import org.springframework.stereotype.Service

@Service
class SessionListener {

    @Autowired
    private lateinit var redisIndexedSessionRepository: RedisIndexedSessionRepository

    @Autowired
    private lateinit var findByIndexNameSessionRepository: FindByIndexNameSessionRepository<out Session>

    @Autowired
    private lateinit var redisOAuth2AuthorizationCompleteRepository: RedisOAuth2AuthorizationCompleteRepository

    @Autowired
    private lateinit var backchannelEventDispatcher: BackchannelEventDispatcher

    @Autowired
    private lateinit var jwtLogoutGenerator: JwtLogoutGenerator

    @Autowired
    private lateinit var registeredClientRepository: RegisteredClientRepository


    private val logger = org.slf4j.LoggerFactory.getLogger(javaClass)

    @EventListener
    fun onSessionCreated(session: SessionCreatedEvent) {
        logger.info("Session created: ${session.sessionId}")
    }

    @EventListener
    fun onSessionDeleted(session: SessionDeletedEvent) {
        logger.info("Session deleted: ${session.sessionId}")
    }

    @EventListener
    fun onSessionExpired(event: SessionExpiredEvent) {
        logger.info("Session expired: ${event.sessionId}");

        val authorization = event.getSession<Session>().getRequiredAttribute<SecurityContext>("SPRING_SECURITY_CONTEXT")

        val principalUsername: String = authorization.getPrincipalUsername() ?: return

        val clientSessions = redisOAuth2AuthorizationCompleteRepository.findAllByPrincipalName(principalUsername)
            .map {
                val oidcToken = it.oidcToken
                    ?: return@map it.id

                val oAuth2Authorization = it.authorization ?: return@map it.id

                val registeredClient = registeredClientRepository.findById(oAuth2Authorization.registeredClientId) ?: return@map it.id

                val oidcLogoutContext = OidcLogoutContext.Builder()
                    .oidcIdToken(oidcToken)
                    .registeredClient(registeredClient)
                    .sessionId(event.sessionId)
                    .principalName(principalUsername)
                    .build()

                val jwt = jwtLogoutGenerator.generate(oidcLogoutContext)

                backchannelEventDispatcher.dispatch(BackchannelLogoutEvent(jwt, registeredClient))

                return@map it.id
            }

        if (clientSessions.isNotEmpty()) {
            redisOAuth2AuthorizationCompleteRepository.deleteAllById(clientSessions)
        }

        findByIndexNameSessionRepository.findByPrincipalName(principalUsername).forEach { redisSession ->
            redisIndexedSessionRepository.deleteById(redisSession.key)
        }
    }

}