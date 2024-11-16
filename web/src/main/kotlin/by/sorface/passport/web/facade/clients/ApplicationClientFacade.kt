package by.sorface.passport.web.facade.clients

import by.sorface.passport.web.records.requests.ApplicationClientPatchRequest
import by.sorface.passport.web.records.requests.ApplicationRegistry
import by.sorface.passport.web.records.responses.ApplicationClient
import by.sorface.passport.web.records.responses.ApplicationClientRefreshSecret
import java.util.*

interface ApplicationClientFacade {
    fun findByCurrentUser(): List<ApplicationClient>

    fun getByIdAndCurrentUser(clientId: UUID): ApplicationClient?

    fun registry(registryClient: ApplicationRegistry): ApplicationClient?

    fun partialUpdate(clientId: UUID, request: ApplicationClientPatchRequest): ApplicationClient?

    fun refreshSecret(clientId: UUID): ApplicationClientRefreshSecret?

    fun delete(clientId: UUID)
}
