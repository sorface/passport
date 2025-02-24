package by.devpav.kotlin.oidcidp.dao.sql.repository.client

import by.devpav.kotlin.oidcidp.dao.sql.model.client.ClientAuthenticationGrantTypeModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.BaseRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientAuthenticationGrantTypeRepository : BaseRepository<ClientAuthenticationGrantTypeModel> {

    fun findFirstByGrantTypeEndingWithIgnoreCase(grantType: String): ClientAuthenticationGrantTypeModel?

}