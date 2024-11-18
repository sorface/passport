package by.sorface.passport.web.services.users

import by.sorface.passport.web.dao.sql.models.RoleEntity
import by.sorface.passport.web.dao.sql.repository.RoleRepository
import org.springframework.stereotype.Service

@Service
open class DefaultRoleService(private val roleRepository: RoleRepository) : RoleService {

    override fun findByValue(value: String): RoleEntity? = roleRepository.findByValueIgnoreCase(value).orElse(null)

}
