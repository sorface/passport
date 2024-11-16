package by.sorface.passport.web.dao.sql

import by.sorface.passport.web.dao.models.TokenEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RegistryTokenRepository : BaseRepository<TokenEntity?> {

    fun findByHashIgnoreCase(hash: String?): TokenEntity?

    @Query("select t from TokenEntity t where t.user.email = ?1")
    fun findByUserEmail(email: String?): TokenEntity?

    fun deleteByHashIgnoreCase(hash: String?)

}
