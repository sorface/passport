package by.sorface.passport.web.facade.session

import by.sorface.passport.web.constants.SessionAttributes
import by.sorface.passport.web.records.principals.DefaultPrincipal
import by.sorface.passport.web.records.sessions.CleanupSession
import by.sorface.passport.web.records.sessions.UserContextSession
import by.sorface.passport.web.records.sessions.UserSession
import by.sorface.passport.web.services.sessions.AccountSessionService
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.session.Session
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import java.util.stream.Collectors

@Slf4j
@Service
@RequiredArgsConstructor
class DefaultAccountSessionFacade(
    private val accountSessionService: AccountSessionService,
    private val userAgentAnalyzer: UserAgentAnalyzer
) : AccountSessionFacade {

    override fun findByUsername(username: String): UserContextSession {
        val sessions = accountSessionService.findByUsername(username)

        val sessionId = RequestContextHolder.currentRequestAttributes().sessionId

        val userSessions = sessions.map { session: Session -> this.buildUserSession(sessionId, session) }.toList()

        return UserContextSession(userSessions)
    }

    override fun getCurrentActiveSessions(): UserContextSession {
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

        accountSessionService.batchDelete(sessionsForDelete.map { obj: Session -> obj.id })

        val userSessions = sessions.stream()
            .map { session: Session -> this.buildUserSession(sessionId, session) }
            .toList()

        return UserContextSession(userSessions)
    }

    override fun batchDelete(cleanupSession: CleanupSession) = accountSessionService.batchDelete(cleanupSession.sessions)

    private fun buildUserSession(activeId: String, session: Session): UserSession {
        val attribute = session.getAttribute<String>(SessionAttributes.USER_AGENT)

        val immutableUserAgent = userAgentAnalyzer.parse(attribute)

        return UserSession.builder()
            .id(session.id)
            .createdAt(session.creationTime.toEpochMilli())
            .browser(immutableUserAgent.getValue("AgentName"))
            .deviceBrand(immutableUserAgent.getValue("DeviceBrand"))
            .deviceType(immutableUserAgent.getValue("DeviceClass"))
            .device(immutableUserAgent.getValue("DeviceName"))
            .active(activeId.equals(session.id, ignoreCase = true))
            .build()
    }
}
