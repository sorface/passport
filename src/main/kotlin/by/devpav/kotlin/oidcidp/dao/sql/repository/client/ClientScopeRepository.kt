package by.devpav.kotlin.oidcidp.dao.sql.repository.client

import by.devpav.kotlin.oidcidp.dao.sql.model.client.ClientScopeModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.BaseRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientScopeRepository : BaseRepository<ClientScopeModel> {

    fun findFirstByScope(scope: String): ClientScopeModel?

}