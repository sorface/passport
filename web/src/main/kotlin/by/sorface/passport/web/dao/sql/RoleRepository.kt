package by.sorface.passport.web.dao.sql

import by.sorface.passport.web.dao.models.RoleEntity
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : BaseRepository<RoleEntity?> {

    fun findByValueIgnoreCase(value: String?): Optional<RoleEntity?>

}
