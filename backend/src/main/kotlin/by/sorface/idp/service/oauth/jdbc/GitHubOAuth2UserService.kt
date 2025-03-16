package by.sorface.idp.service.oauth.jdbc

import by.sorface.idp.config.security.oauth2.records.GitHubOAuth2User
import by.sorface.idp.dao.sql.model.enums.ProviderType
import by.sorface.idp.dao.sql.repository.user.RoleRepository
import by.sorface.idp.dao.sql.repository.user.UserRepository
import by.sorface.idp.service.oauth.SocialOAuth2UserService
import org.springframework.stereotype.Service

@Service
class GitHubOAuth2UserService(
    userRepository: UserRepository,
    roleRepository: RoleRepository
) : AbstractSocialOAuth2UserService<GitHubOAuth2User>(ProviderType.GITHUB, userRepository, roleRepository), SocialOAuth2UserService<GitHubOAuth2User>