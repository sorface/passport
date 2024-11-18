package by.sorface.passport.web.dao.sql.repository

import by.sorface.passport.web.dao.sql.models.RoleEntity
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : BaseRepository<RoleEntity?> {

    fun findByValueIgnoreCase(value: String?): Optional<RoleEntity?>

}
