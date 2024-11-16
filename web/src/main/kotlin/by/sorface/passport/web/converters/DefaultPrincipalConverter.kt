package by.sorface.passport.web.converters

import by.sorface.passport.web.dao.models.RoleEntity
import by.sorface.passport.web.dao.models.UserEntity
import by.sorface.passport.web.records.principals.DefaultPrincipal
import org.apache.logging.log4j.util.Strings
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors

@Component
class DefaultPrincipalConverter : PrincipalConverter {

    override fun convert(user: UserEntity): DefaultPrincipal {
        val authorities = this.convertRoles(user.roles)

        val userPassword = Optional.ofNullable(user.password).orElse(Strings.EMPTY)

        val sorfaceUser = DefaultPrincipal(user.username, userPassword, true, authorities)

        run {
            sorfaceUser.id = user.id
            sorfaceUser.firstName = user.firstName
            sorfaceUser.lastName = user.lastName
            sorfaceUser.middleName = user.middleName
            sorfaceUser.avatarUrl = user.avatarUrl
            sorfaceUser.email = user.email
            sorfaceUser.birthday = user.birthday
            sorfaceUser.confirm = user.confirm
        }

        return sorfaceUser
    }

    private fun convertRoles(roles: Collection<RoleEntity?>): List<GrantedAuthority> {
        return roles.filterNotNull().map { SimpleGrantedAuthority(it.value) }.toList()
    }
}
