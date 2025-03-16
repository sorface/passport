package by.sorface.idp.dao.sql.repository.client

import by.sorface.idp.dao.sql.model.client.RegisteredClientModel
import by.sorface.idp.dao.sql.repository.BaseRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JdbcRegisteredClientRepository : BaseRepository<RegisteredClientModel> {

    fun findByClientId(id: String?): RegisteredClientModel?

    @Query("select o from RegisteredClientModel o where o.clientId = ?1 and o.createdBy.id = ?2")
    fun findByClientIdAndCreatedById(clientId: UUID?, userId: UUID?): RegisteredClientModel?

    fun findFirstByIdAndCreatedBy_Id(id: UUID, createdById: UUID): RegisteredClientModel?

    @Query("select o from RegisteredClientModel o where o.createdBy.id = ?1")
    fun findAllByCreatedById(userId: UUID?): List<RegisteredClientModel>

    @Query("select r from RegisteredClientModel r where r.createdBy.id = ?1")
    fun findByCreatedUser(createdById: UUID): MutableList<RegisteredClientModel>

}
