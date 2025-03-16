package by.sorface.idp.dao.sql.repository.client

import by.sorface.idp.dao.sql.model.client.ClientAuthenticationMethodModel
import by.sorface.idp.dao.sql.repository.BaseRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientAuthenticationMethodRepository : BaseRepository<ClientAuthenticationMethodModel> {

    fun findFirstByMethodEndingWithIgnoreCase(method: String): ClientAuthenticationMethodModel?

}