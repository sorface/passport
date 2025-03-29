package by.sorface.idp.web.rest.facade.impl

import by.sorface.idp.extencions.getPrincipalUsername
import by.sorface.idp.records.I18Codes
import by.sorface.idp.utils.json.mask.MaskerFields
import by.sorface.idp.web.rest.exceptions.I18RestException
import by.sorface.idp.web.rest.facade.AccountSessionFacade
import by.sorface.idp.web.rest.mapper.UserSessionConverter
import by.sorface.idp.web.rest.model.sessions.AccountCleanupSessionRequest
import by.sorface.idp.web.rest.model.sessions.AccountContextSession
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder

@Service
class DefaultAccountSessionFacade(private val sessionRepository: FindByIndexNameSessionRepository<out Session>) :
    AccountSessionFacade {

        private val logger = LoggerFactory.getLogger(DefaultAccountSessionFacade::class.java)

    @Autowired
    private lateinit var userSessionConverter: UserSessionConverter

    override fun getAll(): AccountContextSession {
        val username = SecurityContextHolder.getContext().getPrincipalUsername() ?: throw I18RestException(
            message = "User isn't authenticated",
            i18Code = I18Codes.I18AuthenticationCodes.NOT_AUTHENTICATED,
            httpStatus = HttpStatus.UNAUTHORIZED
        )

        logger.info("getting all sessions for user: ${MaskerFields.USERNAME.mask(username)}")

        return getAllByUsername(username)
    }

    override fun getAllByUsername(username: String): AccountContextSession {
        val sessionId = RequestContextHolder.currentRequestAttributes().sessionId

        val userSessions = sessionRepository.findByPrincipalName(username)
            .map { entry: Map.Entry<String, Session> -> entry.value }
            .map { session: Session -> userSessionConverter.convert(sessionId, session) }

        logger.debug("found ${userSessions.size} sessions for user: ${MaskerFields.USERNAME.mask(username)}")

        return AccountContextSession(userSessions)
    }

    override fun deleteMultiple(accountCleanupSessionRequest: AccountCleanupSessionRequest): AccountContextSession {
        val username = SecurityContextHolder.getContext().getPrincipalUsername() ?: throw I18RestException(
            message = "User isn't authenticated",
            i18Code = I18Codes.I18AuthenticationCodes.NOT_AUTHENTICATED,
            httpStatus = HttpStatus.UNAUTHORIZED
        )

        logger.info("deleting multiple sessions for user: ${MaskerFields.USERNAME.mask(username)}")

        return deleteMultipleByUsername(username, accountCleanupSessionRequest)
    }

    override fun deleteMultipleByUsername(username: String, accountCleanupSessionRequest: AccountCleanupSessionRequest): AccountContextSession {
        val deletedSessions = HashSet(accountCleanupSessionRequest.sessions)

        val sessions = sessionRepository.findByPrincipalName(username).values

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

        logger.info("deleted ${deletedSessions.size} sessions for user: ${MaskerFields.USERNAME.mask(username)}")
        logger.debug("remaining sessions for user: {} are {}", MaskerFields.USERNAME.mask(username), userSessions)

        return AccountContextSession(userSessions)
    }
}
