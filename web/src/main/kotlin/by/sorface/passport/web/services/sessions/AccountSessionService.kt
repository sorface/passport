package by.sorface.passport.web.services.sessions

import org.springframework.session.Session

interface AccountSessionService {

    fun findByUsername(username: String): List<Session>

    fun batchDelete(sessionIds: List<String?>)

}
