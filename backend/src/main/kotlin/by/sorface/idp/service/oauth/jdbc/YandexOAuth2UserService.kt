package by.sorface.idp.service.oauth.jdbc

import by.sorface.idp.config.security.oauth2.records.YandexOAuth2User
import by.sorface.idp.dao.sql.model.enums.ProviderType
import by.sorface.idp.dao.sql.repository.user.RoleRepository
import by.sorface.idp.dao.sql.repository.user.UserRepository
import by.sorface.idp.service.oauth.SocialOAuth2UserService
import org.springframework.stereotype.Service

@Service
class YandexOAuth2UserService(
    userRepository: UserRepository,
    roleRepository: RoleRepository
) : AbstractSocialOAuth2UserService<YandexOAuth2User>(ProviderType.YANDEX, userRepository, roleRepository), SocialOAuth2UserService<YandexOAuth2User>

