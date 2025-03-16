package by.sorface.idp.service.oauth.converters

import by.sorface.idp.dao.sql.model.RoleModel
import by.sorface.idp.dao.sql.model.UserModel
import by.sorface.idp.records.SorfacePrincipal
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
