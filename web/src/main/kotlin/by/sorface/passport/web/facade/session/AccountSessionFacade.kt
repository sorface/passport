package by.sorface.passport.web.facade.session

import by.sorface.passport.web.records.sessions.CleanupSession
import by.sorface.passport.web.records.sessions.UserContextSession

interface AccountSessionFacade {

    fun findByUsername(username: String): UserContextSession?

    fun getActiveSessions(): UserContextSession?

    fun deleteSessions(cleanupSession: CleanupSession): UserContextSession?

    fun batchDelete(cleanupSession: CleanupSession): Set<String>

}
