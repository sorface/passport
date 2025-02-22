package by.devpav.kotlin.oidcidp.web.rest.facade

import by.devpav.kotlin.oidcidp.extencions.getPrincipalUsername
import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.records.SorfacePrincipal
import by.devpav.kotlin.oidcidp.web.rest.exceptions.I18RestException
import by.devpav.kotlin.oidcidp.web.rest.mapper.UserSessionConverter
import by.devpav.kotlin.oidcidp.web.rest.model.sessions.AccountCleanupSessionRequest
import by.devpav.kotlin.oidcidp.web.rest.model.sessions.AccountContextSession
import by.sorface.passport.web.facade.session.AccountSessionFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder

@Service
class DefaultAccountSessionFacade(private val sessionRepository: FindByIndexNameSessionRepository<out Session>) : AccountSessionFacade {

    @Autowired
    private lateinit var userSessionConverter: UserSessionConverter

    override fun findByUsername(username: String): AccountContextSession {
        val sessions = sessionRepository.findByPrincipalName(username)

        val sessionId = RequestContextHolder.currentRequestAttributes().sessionId

        val userSessions = sessions
            .map { entry: Map.Entry<String, Session> -> entry.value }
            .map { session: Session -> userSessionConverter.convert(sessionId, session) }
            .toList()

        return AccountContextSession(userSessions)
    }

    override fun getActive(): AccountContextSession {
        val authentication = SecurityContextHolder.getContext().authentication

        val principal = authentication.principal as SorfacePrincipal

        val sessionId = RequestContextHolder.currentRequestAttributes().sessionId

        val userSessions = sessionRepository.findByPrincipalName(principal.username)
            .map { entry: Map.Entry<String, Session> -> entry.value }
            .map { session: Session -> userSessionConverter.convert(sessionId, session) }

        return AccountContextSession(userSessions)
    }

    override fun deleteSessions(accountCleanupSessionRequest: AccountCleanupSessionRequest): AccountContextSession {
        val principalUsername = SecurityContextHolder.getContext().getPrincipalUsername()

        val deletedSessions = HashSet(accountCleanupSessionRequest.sessions)

        val sessions = sessionRepository.findByPrincipalName(principalUsername).values

        val unknownSessions = deletedSessions
            .filter { sessionId -> sessions.stream().anyMatch { session: Session -> sessionId == session.id } }

        if (unknownSessions.isNotEmpty()) {
            throw I18RestException(
                message = "Unknown session ID $unknownSessions",
                i18Code = I18Codes.I18SessionCodes.UNKNOWN_SESSION_ID,
                i18Args = mapOf("sessionId" to unknownSessions.joinToString { ", " }),
                httpStatus = HttpStatus.BAD_REQUEST
            )
        }

        val sessionId = RequestContextHolder.currentRequestAttributes().sessionId

        deletedSessions.forEach { sessionRepository.deleteById(it) }

        val userSessions = sessions
            .map { session: Session -> userSessionConverter.convert(sessionId, session) }
            .toList()

        return AccountContextSession(userSessions)
    }

}
