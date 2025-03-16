package by.sorface.idp.web.graphql.services.impl

import by.sorface.idp.graphql.model.GQSession
import by.sorface.idp.graphql.model.GQUserSession
import by.sorface.idp.web.graphql.services.SessionService
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder

@Service
class DefaultSessionService(private val sessionRepository: FindByIndexNameSessionRepository<out org.springframework.session.Session>) : SessionService {

    override fun getAllByUsername(username: String): List<GQSession> =
        sessionRepository.findByPrincipalName(username)
            .mapNotNull { entry -> entry.value }
            .map { session -> buildUserSession(RequestContextHolder.currentRequestAttributes().sessionId, session) }
            .toList()

    private fun <T : org.springframework.session.Session> buildUserSession(activeId: String, session: T): GQSession {
        return GQSession()
            .apply {
                id = session.id
                createdAt = session.creationTime.toEpochMilli()
                active = activeId.equals(session.id, ignoreCase = true)
            }
    }

}