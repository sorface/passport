package by.sorface.passport.web.security.oauth2.services

import by.sorface.passport.web.services.users.UserService
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.stereotype.Component

@Component
class OidcUserDatabaseProvider(private val userService: UserService) {

    fun loadUser(username: String, scopes: Set<String>): OidcUserInfo {
        val builder = OidcUserInfo.builder().subject(username)

        if (scopes.isEmpty()) {
            return builder.build()
        }

        val user = userService.findByUsername(username) ?: return builder.build()

        if (scopes.contains(OidcScopes.PROFILE)) {
            return builder.nickname(username)
                .email(user.email)
                .picture(user.avatarUrl)
                .emailVerified(user.confirm)
                .build()
        }

        if (scopes.contains(OidcScopes.EMAIL)) {
            return builder.email(user.email).build()
        }

        return builder.build()
    }

}
