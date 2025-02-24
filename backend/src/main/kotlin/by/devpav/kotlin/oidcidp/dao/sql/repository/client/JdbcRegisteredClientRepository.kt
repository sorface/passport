package by.devpav.kotlin.oidcidp.dao.sql.repository.client

import by.devpav.kotlin.oidcidp.dao.sql.model.client.RegisteredClientModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.BaseRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JdbcRegisteredClientRepository : BaseRepository<RegisteredClientModel> {

    fun findByClientId(id: String?): RegisteredClientModel?

    @Query("select o from RegisteredClientModel o where o.id = ?1 and o.createdBy.id = ?2")
    fun findByIdAndCreatedById(oauth2ClientId: UUID?, userId: UUID?): RegisteredClientModel?

    @Query("select o from RegisteredClientModel o where o.createdBy.id = ?1")
    fun findAllByCreatedById(userId: UUID?): List<RegisteredClientModel>

    @Query("select r from RegisteredClientModel r where r.createdBy.id = ?1")
    fun findByCreatedUser(createdById: UUID): MutableList<RegisteredClientModel>

}
