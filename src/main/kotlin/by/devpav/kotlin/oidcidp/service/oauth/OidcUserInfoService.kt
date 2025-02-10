package by.devpav.kotlin.oidcidp.service.oauth

import by.devpav.kotlin.oidcidp.dao.sql.repository.user.UserRepository
import by.devpav.kotlin.oidcidp.exceptions.GraphqlServerException
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Сервис для загрузки информации о пользователе в формате OidcUserInfo.
 */
@Service
class OidcUserInfoService(private val userRepository: UserRepository) {

    /**
     * Загружает информацию о пользователе на основе его имени и набора областей.
     *
     * @param name Имя пользователя.
     * @param scopes Набор областей.
     * @return Информация о пользователе в формате OidcUserInfo.
     */
    fun loadUser(name: String, scopes: Set<String?>): OidcUserInfo {
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
                    .updatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
            }
            scopes.contains(OidcScopes.EMAIL) -> {
                builder.email(user.email)
                    .emailVerified(user.confirm)
            }
        }

        return builder.build()
    }

}

