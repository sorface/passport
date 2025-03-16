package by.sorface.idp.service.oauth.providers

import by.sorface.idp.config.security.oauth2.records.GitHubOAuth2User
import by.sorface.idp.service.oauth.jdbc.GitHubOAuth2UserService
import by.sorface.idp.service.oauth.converters.GitHubOAuth2UserConverter
import by.sorface.idp.service.oauth.converters.PrincipalConverter
import org.springframework.stereotype.Service

@Service
class GitHubOAuth2UserDatabaseProvider(
    gitHubOAuth2UserService: GitHubOAuth2UserService,
    principalConverter: PrincipalConverter,
    gitHubOAuth2UserConverter: GitHubOAuth2UserConverter
) : AbstractOAuth2UserDatabaseProvider<GitHubOAuth2User>(gitHubOAuth2UserService, principalConverter, gitHubOAuth2UserConverter)