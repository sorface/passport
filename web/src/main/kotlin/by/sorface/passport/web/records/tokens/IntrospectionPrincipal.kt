package by.sorface.passport.web.records.tokens

import by.sorface.passport.web.records.principals.DefaultPrincipal
import org.springframework.security.core.GrantedAuthority
import java.time.LocalDate
import java.util.*

class IntrospectionPrincipal {
    var id: UUID? = null
    var firstName: String? = null
    var lastName: String? = null
    var middleName: String? = null
    var birthday: LocalDate? = null
    var avatarUrl: String? = null
    var username: String? = null
    var email: String? = null
    var authorities: List<String>? = null

    companion object {
        fun from(authorizedUser: DefaultPrincipal?): IntrospectionPrincipal? {
            if (authorizedUser == null) {
                return null
            }

            val authorities = authorizedUser.authorities?.let { authorities ->
                authorities.map { obj: GrantedAuthority -> obj.authority }
            } ?: listOf()

            val introspectionPrincipal = IntrospectionPrincipal()

            introspectionPrincipal.id = authorizedUser.id
            introspectionPrincipal.firstName = authorizedUser.firstName
            introspectionPrincipal.lastName = authorizedUser.lastName
            introspectionPrincipal.middleName = authorizedUser.middleName
            introspectionPrincipal.birthday = authorizedUser.birthday
            introspectionPrincipal.avatarUrl = authorizedUser.avatarUrl
            introspectionPrincipal.username = authorizedUser.username
            introspectionPrincipal.email = authorizedUser.email
            introspectionPrincipal.authorities = authorities

            return introspectionPrincipal
        }
    }
}
