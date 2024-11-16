package by.sorface.passport.web.services.users

import by.sorface.passport.web.dao.models.RoleEntity
import by.sorface.passport.web.dao.sql.RoleRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
open class DefaultRoleService(
    private val roleRepository: RoleRepository
) : RoleService {

    override fun findByValue(value: String): RoleEntity? {
        return roleRepository.findByValueIgnoreCase(value).orElse(null)
    }

}
