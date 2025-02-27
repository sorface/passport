package by.devpav.kotlin.oidcidp.web.graphql.services.impl

import by.devpav.kotlin.oidcidp.graphql.model.Session
import by.devpav.kotlin.oidcidp.graphql.model.UserSession
import by.devpav.kotlin.oidcidp.web.graphql.services.SessionService
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder

@Service
class DefaultSessionService(private val sessionRepository: FindByIndexNameSessionRepository<out org.springframework.session.Session>) : SessionService {

    override fun getAllByUsername(username: String): List<Session> =
        sessionRepository.findByPrincipalName(username)
            .mapNotNull { entry -> entry.value }
            .map { session -> buildUserSession(RequestContextHolder.currentRequestAttributes().sessionId, session) }
            .toList()

    private fun <T : org.springframework.session.Session> buildUserSession(activeId: String, session: T): Session {
        return Session()
            .apply {
                id = session.id
                createdAt = session.creationTime.toEpochMilli()
                active = activeId.equals(session.id, ignoreCase = true)
            }
    }

    private fun <T : org.springframework.session.Session> buildSession(session: T): UserSession {
        return UserSession()
            .apply {
                id = session.id
                createdAt = session.creationTime.toEpochMilli()
            }
    }
}