package by.sorface.passport.web.dao.sql.repository

import by.sorface.passport.web.dao.sql.models.OAuth2Client
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OAuth2ClientRepository : BaseRepository<OAuth2Client?> {

    fun findByClientId(id: String?): OAuth2Client?

    @Query("select o from OAuth2Client o where o.id = ?1 and o.createdBy.id = ?2")
    fun findByIdAndCreatedById(oauth2ClientId: UUID?, userId: UUID?): OAuth2Client?

    @Query("select o from OAuth2Client o where o.createdBy.id = ?1")
    fun findAllByCreatedById(userId: UUID?): List<OAuth2Client>

}
