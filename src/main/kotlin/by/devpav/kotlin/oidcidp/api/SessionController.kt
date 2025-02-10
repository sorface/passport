package by.devpav.kotlin.oidcidp.api

import by.devpav.kotlin.oidcidp.exceptions.GraphqlUserException
import by.devpav.kotlin.oidcidp.extencions.getPrincipalOrThrow
import by.devpav.kotlin.oidcidp.graphql.UserSession
import by.devpav.kotlin.oidcidp.records.I18Codes
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session
import org.springframework.stereotype.Controller
import org.springframework.web.context.request.RequestContextHolder

@Controller
class SessionController(
    private val sessionRepository: FindByIndexNameSessionRepository<out Session>
) {

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    fun sessionGetAllByUser() : List<by.devpav.kotlin.oidcidp.graphql.Session> {
        val principal = SecurityContextHolder.getContext().getPrincipalOrThrow(GraphqlUserException(I18Codes.I18GlobalCodes.ACCESS_DENIED))

        return sessionRepository.findByPrincipalName(principal.username)
            .mapNotNull { entry -> entry.value }
            .map { session ->  buildUserSession(RequestContextHolder.currentRequestAttributes().sessionId, session) }
            .toList()
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun sessionGetAllByUsername(@Argument username: String) : List<UserSession> {
        return sessionRepository.findByPrincipalName(username)
            .mapNotNull { entry -> entry.value }
            .map { session ->  buildSession(session) }
            .toList()
    }

    private fun <T : Session> buildUserSession(activeId: String, session: T): by.devpav.kotlin.oidcidp.graphql.Session {
        return by.devpav.kotlin.oidcidp.graphql.Session()
            .apply {
                id = session.id
                createdAt = session.creationTime.toEpochMilli()
                active = activeId.equals(session.id, ignoreCase = true)
            }
    }

    private fun <T : Session> buildSession(session: T): UserSession {
        return UserSession()
            .apply {
                id = session.id
                createdAt = session.creationTime.toEpochMilli()
            }
    }
}