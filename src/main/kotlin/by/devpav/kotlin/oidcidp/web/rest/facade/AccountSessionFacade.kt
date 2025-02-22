package by.sorface.passport.web.facade.session

import by.devpav.kotlin.oidcidp.web.rest.model.sessions.AccountCleanupSessionRequest
import by.devpav.kotlin.oidcidp.web.rest.model.sessions.AccountContextSession

interface AccountSessionFacade {

    fun findByUsername(username: String): AccountContextSession?

    fun getActive(): AccountContextSession?

    fun deleteSessions(accountCleanupSessionRequest: AccountCleanupSessionRequest): AccountContextSession?

}
