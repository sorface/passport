package by.devpav.kotlin.oidcidp.service.oauth.converters

import by.devpav.kotlin.oidcidp.dao.sql.model.RoleModel
import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.records.SorfacePrincipal
import org.springframework.stereotype.Component

@Component
class DefaultPrincipalConverter : PrincipalConverter {

    override fun convert(user: UserModel): SorfacePrincipal {
        val authorities = this.convertRoles(user.roles)

        return SorfacePrincipal(
            user.id,
            user.username!!,
            user.password,
            authorities
        )
    }

    private fun convertRoles(roles: Collection<RoleModel>): Set<String> {
        return roles.mapNotNull { role -> role.value }.toSet()
    }
}
