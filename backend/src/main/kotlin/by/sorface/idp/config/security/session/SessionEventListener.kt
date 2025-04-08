package by.sorface.idp.config.security.session

import by.sorface.idp.extencions.getPrincipalUsername
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME
import org.springframework.session.Session
import org.springframework.session.events.SessionCreatedEvent
import org.springframework.session.events.SessionDeletedEvent
import org.springframework.session.events.SessionExpiredEvent
import org.springframework.stereotype.Service

@Service
class SessionEventListener(private val sessionManager: SessionManager) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun onSessionCreated(session: SessionCreatedEvent) {
        logger.info("session [id -> ${session.sessionId}] created")
    }

    @EventListener
    fun onSessionDeleted(session: SessionDeletedEvent) {
        logger.info("session [id -> ${session.sessionId}] deleted")
    }

    @EventListener
    fun onSessionExpired(event: SessionExpiredEvent) {
        val authorization = event.getSession<Session>().getRequiredAttribute<SecurityContext>(DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME)

        val principalUsername: String = authorization.getPrincipalUsername() ?: return

        logger.info("session [id -> ${event.sessionId}] expired for user [username -> $principalUsername]");

        sessionManager.resetWithNotify(event.sessionId, principalUsername)
    }

}