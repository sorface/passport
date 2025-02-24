package by.devpav.kotlin.oidcidp.dao.sql.repository.client

import by.devpav.kotlin.oidcidp.dao.sql.model.client.ClientAuthenticationMethodModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.BaseRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientAuthenticationMethodRepository : BaseRepository<ClientAuthenticationMethodModel> {

    fun findFirstByMethodEndingWithIgnoreCase(method: String): ClientAuthenticationMethodModel?

}