package by.sorface.idp.dao.sql.repository.client

import by.sorface.idp.dao.sql.model.client.ClientAuthenticationGrantTypeModel
import by.sorface.idp.dao.sql.repository.BaseRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientAuthenticationGrantTypeRepository : BaseRepository<ClientAuthenticationGrantTypeModel> {

    fun findFirstByGrantTypeEndingWithIgnoreCase(grantType: String): ClientAuthenticationGrantTypeModel?

}