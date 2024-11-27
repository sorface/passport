package by.sorface.passport.web.facade.session

import by.sorface.passport.web.exceptions.UserRequestException
import by.sorface.passport.web.records.I18Codes.I18SessionCodes.DELETE_ACTIVE_SESSION_ERROR
import by.sorface.passport.web.records.principals.DefaultPrincipal
import by.sorface.passport.web.records.sessions.CleanupSession
import by.sorface.passport.web.records.sessions.UserContextSession
import by.sorface.passport.web.records.sessions.UserSession
import by.sorface.passport.web.security.constants.SessionAttributes
import by.sorface.passport.web.security.oauth2.services.AccountSessionService
import nl.basjes.parse.useragent.UserAgent
import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.session.Session
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder

@Service
class DefaultAccountSessionFacade(private val accountSessionService: AccountSessionService, private val userAgentAnalyzer: UserAgentAnalyzer) : AccountSessionFacade {

    override fun findByUsername(username: String): UserContextSession {
        val sessions = accountSessionService.findByUsername(username)

        val sessionId = RequestContextHolder.currentRequestAttributes().sessionId

        val userSessions = sessions.map { session: Session -> this.buildUserSession(sessionId, session) }.toList()

        return UserContextSession(userSessions)
    }

    override fun getActiveSessions(): UserContextSession {
        val authentication = SecurityContextHolder.getContext().authentication

        val principal = authentication.principal as DefaultPrincipal

        val sessionId = RequestContextHolder.currentRequestAttributes().sessionId

        val userSessions = accountSessionService.findByUsername(principal.username).map { session: Session -> this.buildUserSession(sessionId, session) }

        return UserContextSession(userSessions)
    }

    override fun deleteSessions(cleanupSession: CleanupSession): UserContextSession {
        val deletedSessions = HashSet(cleanupSession.sessions)

        val authentication = SecurityContextHolder.getContext().authentication

        val principal = authentication.principal as DefaultPrincipal

        val sessions = accountSessionService.findByUsername(principal.username)

        val sessionsForDelete = sessions.filter { it: Session -> deletedSessions.contains(it.id) }.toList()

        val sessionId = RequestContextHolder.currentRequestAttributes().sessionId

        accountSessionService.batchDeleteById(sessionsForDelete.map { obj: Session -> obj.id })

        val userSessions = sessions.stream()
            .map { session: Session -> this.buildUserSession(sessionId, session) }
            .toList()

        return UserContextSession(userSessions)
    }

    override fun batchDelete(cleanupSession: CleanupSession): Set<String> {
        val currentSessionId = RequestContextHolder.currentRequestAttributes().sessionId

        if (cleanupSession.sessions.contains(currentSessionId)) {
            throw UserRequestException(DELETE_ACTIVE_SESSION_ERROR)
        }

        return accountSessionService.batchDeleteById(cleanupSession.sessions)
    }

    private fun buildUserSession(activeId: String, session: Session): UserSession {
        val attribute = session.getAttribute<String>(SessionAttributes.USER_AGENT)

        val immutableUserAgent = userAgentAnalyzer.parse(attribute)

        return UserSession()
            .apply {
                id = session.id
                createdAt = session.creationTime.toEpochMilli()
                browser = immutableUserAgent.getValue(UserAgent.AGENT_NAME)
                deviceBrand = immutableUserAgent.getValue(UserAgent.DEVICE_BRAND)
                deviceType = immutableUserAgent.getValue(UserAgent.DEVICE_CLASS)
                device = immutableUserAgent.getValue(UserAgent.DEVICE_NAME)
                active = activeId.equals(session.id, ignoreCase = true)
            }
    }
}
