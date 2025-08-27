package by.sorface.idp.config.security.session

import by.sorface.idp.extencions.getPrincipalUsername
import by.sorface.idp.records.events.SessionRefreshEvent
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME
import org.springframework.session.Session
import org.springframework.session.data.redis.RedisIndexedSessionRepository
import org.springframework.session.events.SessionCreatedEvent
import org.springframework.session.events.SessionDeletedEvent
import org.springframework.session.events.SessionExpiredEvent
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class SessionEventListener(
    private val sessionManager: SessionManager,
    private val redisIndexedSessionRepository: RedisIndexedSessionRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun onSessionCreated(event: SessionRefreshEvent) {
        logger.info("session [id -> ${event.sessionId}] refreshed")

        try {
            // Получаем сессию из репозитория
            val session = redisIndexedSessionRepository.findById(event.sessionId)

            val beforeLastAccessedTime = session.lastAccessedTime

            if (session != null) {
                // Обновляем last access time через принудительный вызов setLastAccessedTime
                session.setLastAccessedTime(Instant.now())

                // Сохраняем обновленную сессию
                redisIndexedSessionRepository.save(session)

                logger.debug(
                    "Updated last access time for session [id -> {}, before -> {}, after -> {}]",
                    event.sessionId,
                    beforeLastAccessedTime,
                    session.lastAccessedTime
                )
            } else {
                logger.warn("Session [id -> ${event.sessionId}] not found in repository")
            }
        } catch (e: Exception) {
            logger.error("Failed to update last access time for session [id -> ${event.sessionId}]: ${e.message}", e)
        }
    }

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
        val session = event.getSession<Session>()

        val authorization = session.getRequiredAttribute<SecurityContext>(DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME)

        val principalUsername: String = authorization.getPrincipalUsername() ?: return

        logger.info("session [id -> ${event.sessionId}] expired for user [username -> $principalUsername]")

        sessionManager.resetWithNotify(event.sessionId, principalUsername)
    }

}