package by.sorface.passport.web.security.converters

import by.sorface.passport.web.dao.sql.models.RoleEntity
import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.records.principals.DefaultPrincipal
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class DefaultPrincipalConverter : PrincipalConverter {

    override fun convert(user: UserEntity): DefaultPrincipal {
        val authorities = this.convertRoles(user.roles)

        val userPassword = user.password ?: ""

        return DefaultPrincipal(user.username, userPassword, true, authorities)
            .apply {
                id = user.id
                firstName = user.firstName
                lastName = user.lastName
                _middleName = user.middleName
                avatarUrl = user.avatarUrl
                _email = user.email
                birthday = user.birthday
                confirm = user.confirm
            }
    }

    private fun convertRoles(roles: Collection<RoleEntity?>): List<GrantedAuthority> {
        return roles.filterNotNull().map { SimpleGrantedAuthority(it.value) }.toList()
    }
}
