package by.sorface.passport.web.services.users

import by.sorface.passport.web.dao.sql.models.RoleEntity

interface RoleService {

    fun findByValue(value: String): RoleEntity?

}
