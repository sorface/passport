package by.devpav.kotlin.oidcidp.service.oauth.jdbc

import by.devpav.kotlin.oidcidp.config.security.oauth2.records.GitHubOAuth2User
import by.devpav.kotlin.oidcidp.dao.sql.model.enums.ProviderType
import by.devpav.kotlin.oidcidp.dao.sql.repository.user.RoleRepository
import by.devpav.kotlin.oidcidp.dao.sql.repository.user.UserRepository
import by.devpav.kotlin.oidcidp.service.oauth.SocialOAuth2UserService
import org.springframework.stereotype.Service

@Service
class GitHubOAuth2UserService(
    userRepository: UserRepository,
    roleRepository: RoleRepository
) : AbstractSocialOAuth2UserService<GitHubOAuth2User>(ProviderType.GITHUB, userRepository, roleRepository), SocialOAuth2UserService<GitHubOAuth2User>