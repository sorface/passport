package by.sorface.idp.service.oauth.jdbc

import by.sorface.idp.dao.sql.repository.user.UserRepository
import by.sorface.idp.exceptions.GraphqlServerException
import by.sorface.idp.service.OidcUserInfoService
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class DefaultOidcUserInfoService(private val userRepository: UserRepository) : OidcUserInfoService {

    override fun loadUser(name: String, scopes: Set<String?>): OidcUserInfo {
        val builder = OidcUserInfo.builder().subject(name)

        val user = userRepository.findFirstByUsername(name)

        user ?: throw GraphqlServerException("global.unknown_error")

        when {
            scopes.contains(OidcScopes.PROFILE) -> {
                builder.name("${user.id}")
                    .givenName(user.firstName)
                    .familyName(user.lastName)
                    .middleName(user.middleName)
                    .nickname(user.username)
                    .preferredUsername(name)
                    .picture(user.avatarUrl)
                    .birthdate("${user.birthday}")
            }

            scopes.contains(OidcScopes.EMAIL) -> {
                builder.email(user.email)
                    .emailVerified(user.confirm)
            }
        }

        return builder.build()
    }

}

