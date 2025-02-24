package by.devpav.kotlin.oidcidp.dao.sql.repository.user

import by.devpav.kotlin.oidcidp.dao.sql.model.RoleModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.BaseRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : BaseRepository<RoleModel> {

    fun findByValueIgnoreCase(value: String): RoleModel?

}
