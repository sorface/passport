package by.sorface.idp.dao.sql.repository.client

import by.sorface.idp.dao.sql.model.client.ClientScopeModel
import by.sorface.idp.dao.sql.repository.BaseRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientScopeRepository : BaseRepository<ClientScopeModel> {

    fun findFirstByScope(scope: String): ClientScopeModel?

}