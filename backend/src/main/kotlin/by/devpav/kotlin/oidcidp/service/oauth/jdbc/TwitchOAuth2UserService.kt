package by.devpav.kotlin.oidcidp.service.oauth.jdbc

import by.devpav.kotlin.oidcidp.config.security.oauth2.records.TwitchOAuth2User
import by.devpav.kotlin.oidcidp.dao.sql.model.enums.ProviderType
import by.devpav.kotlin.oidcidp.dao.sql.repository.user.RoleRepository
import by.devpav.kotlin.oidcidp.dao.sql.repository.user.UserRepository
import by.devpav.kotlin.oidcidp.service.oauth.SocialOAuth2UserService
import org.springframework.stereotype.Service

@Service
class TwitchOAuth2UserService(
    userRepository: UserRepository,
    roleRepository: RoleRepository
) : AbstractSocialOAuth2UserService<TwitchOAuth2User>(ProviderType.TWITCH, userRepository, roleRepository), SocialOAuth2UserService<TwitchOAuth2User>