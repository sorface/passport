package by.sorface.passport.web.security.oauth2.services

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.dao.sql.models.enums.ProviderType
import by.sorface.passport.web.security.oauth2.records.*
import by.sorface.passport.web.services.users.RoleService
import by.sorface.passport.web.services.users.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface SocialOAuth2UserService<T : ExternalOAuth2User> {

    fun findOrCreate(oAuth2user: T): UserEntity

}

@Service
class GitHubOAuth2UserService(
    userService: UserService,
    roleService: RoleService
) : AbstractSocialOAuth2UserService<GitHubOAuth2User>(ProviderType.GITHUB, userService, roleService), SocialOAuth2UserService<GitHubOAuth2User>

@Service
class GoogleOAuth2UserService(
    userService: UserService,
    roleService: RoleService
) : AbstractSocialOAuth2UserService<GoogleOAuth2User>(ProviderType.GOOGLE, userService, roleService), SocialOAuth2UserService<GoogleOAuth2User>

@Service
class TwitchOAuth2UserService(
    userService: UserService,
    roleService: RoleService
) : AbstractSocialOAuth2UserService<TwitchOAuth2User>(ProviderType.TWITCH, userService, roleService), SocialOAuth2UserService<TwitchOAuth2User>

@Service
class YandexOAuth2UserService(
    userService: UserService,
    roleService: RoleService
) : AbstractSocialOAuth2UserService<YandexOAuth2User>(ProviderType.YANDEX, userService, roleService), SocialOAuth2UserService<YandexOAuth2User>

abstract class AbstractSocialOAuth2UserService<T : ExternalOAuth2User> protected constructor(
    private val providerType: ProviderType,
    private val userService: UserService,
    private val roleService: RoleService
) : SocialOAuth2UserService<T> {

    @Transactional
    override fun findOrCreate(oAuth2user: T): UserEntity {
        val user = userService.findByProviderAndExternalId(providerType, oAuth2user.id!!)

        if (user != null) {
            return user
        }

        val newUser = this.createNewUser(oAuth2user)

        val userRole = roleService.findByValue(DEFAULT_ROLE_USER)
        newUser.roles = listOfNotNull(userRole)

        return userService.save(newUser)
    }

    private fun createNewUser(socialOAuth2User: T): UserEntity {
        val newUser = UserEntity().run {
            username = buildUserName(socialOAuth2User.username!!, socialOAuth2User.id)
            avatarUrl = socialOAuth2User.avatarUrl
            lastName = socialOAuth2User.lastName
            firstName = socialOAuth2User.firstName
            middleName = socialOAuth2User.middleName
            externalId = socialOAuth2User.id
            avatarUrl = socialOAuth2User.avatarUrl

            this
        }

        return newUser
    }

    private fun buildUserName(username: String, id: String?): String {
        val existUsername = userService.isExistUsername(username)

        return if (existUsername) username + "_" + id?.replace("-".toRegex(), "")?.substring(4) else username
    }

    companion object {
        private const val DEFAULT_ROLE_USER = "User"
    }
}
