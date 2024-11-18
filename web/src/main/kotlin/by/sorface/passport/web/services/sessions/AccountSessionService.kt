package by.sorface.passport.web.services.sessions

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
    fun batchDeleteById(sessionIds: List<String>)

}

@Service
class DefaultAccountSessionService(private val sessionRepository: FindByIndexNameSessionRepository<out Session>) : AccountSessionService {
    override fun findByUsername(username: String): List<Session> = (sessionRepository.findByPrincipalName(username) ?: mapOf()).values.map { it }
    override fun findDictionaryByUsername(username: String): Map<String, Session> = sessionRepository.findByPrincipalName(username) ?: mapOf()
    override fun batchDeleteById(sessionIds: List<String>) = sessionIds.toSet().forEach { sessionRepository.deleteById(it) }
}
