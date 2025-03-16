package by.sorface.idp.service.oauth.jdbc

import by.sorface.idp.config.security.oauth2.records.TwitchOAuth2User
import by.sorface.idp.dao.sql.model.enums.ProviderType
import by.sorface.idp.dao.sql.repository.user.RoleRepository
import by.sorface.idp.dao.sql.repository.user.UserRepository
import by.sorface.idp.service.oauth.SocialOAuth2UserService
import org.springframework.stereotype.Service

@Service
class TwitchOAuth2UserService(
    userRepository: UserRepository,
    roleRepository: RoleRepository
) : AbstractSocialOAuth2UserService<TwitchOAuth2User>(ProviderType.TWITCH, userRepository, roleRepository), SocialOAuth2UserService<TwitchOAuth2User>