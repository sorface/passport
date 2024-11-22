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

        val sorfaceUser = DefaultPrincipal(user.username, userPassword, true, authorities)
        sorfaceUser.id = user.id
        sorfaceUser.firstName = user.firstName
        sorfaceUser.lastName = user.lastName
        sorfaceUser._middleName = user.middleName
        sorfaceUser.avatarUrl = user.avatarUrl
        sorfaceUser._email = user.email
        sorfaceUser.birthday = user.birthday
        sorfaceUser.confirm = user.confirm

        return sorfaceUser
    }

    private fun convertRoles(roles: Collection<RoleEntity?>): List<GrantedAuthority> {
        return roles.filterNotNull().map { SimpleGrantedAuthority(it.value) }.toList()
    }
}
