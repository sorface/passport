package by.sorface.passport.web.security.sessions

import org.slf4j.LoggerFactory
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session
import org.springframework.stereotype.Service

/**
 * Контракт взаимодействия с сессиями пользователя
 *
 * @author Павел Талайко
 */
interface AccountSessionService {

    /**
     * Поиск сессий по идентификационному имени пользователя
     *
     * @param username идентификационное имя пользователя
     * @return список сессий пользователя
     */
    fun findByUsername(username: String): List<Session>

    /**
     * Поиск сессий по идентификационному имени пользователя
     *
     * @param username идентификационное имя пользователя
     * @return словарь, где ключ идентификатор сессии, а значение сессия
     */
    fun findDictionaryByUsername(username: String): Map<String, Session>

    /**
     * Удаление сессий по идентификаторам сессий
     *
     * @param sessionIds список идентификаторов, по которым нужно удалить сессии
     */
    fun batchDeleteById(sessionIds: List<String>): Set<String>

}

@Service
class DefaultAccountSessionService(private val sessionRepository: FindByIndexNameSessionRepository<out Session>) : AccountSessionService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun findByUsername(username: String): List<Session> = (sessionRepository.findByPrincipalName(username) ?: mapOf()).values.map { it }
    override fun findDictionaryByUsername(username: String): Map<String, Session> = sessionRepository.findByPrincipalName(username) ?: mapOf()

    override fun batchDeleteById(sessionIds: List<String>): Set<String> {
        return sessionIds.toSet()
            .map { sessionId ->
                sessionRepository.deleteById(sessionId)

                logger.info("delete user session [id -> $sessionId]")

                return@map sessionId
            }
            .toSet()
    }
}
