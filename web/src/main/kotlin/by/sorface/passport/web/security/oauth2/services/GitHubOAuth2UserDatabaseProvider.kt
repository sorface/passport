package by.sorface.passport.web.security.oauth2.services

import by.sorface.passport.web.security.converters.PrincipalConverter
import by.sorface.passport.web.security.oauth2.converter.GitHubOAuth2UserConverter
import by.sorface.passport.web.security.oauth2.records.GitHubOAuth2User
import org.springframework.stereotype.Service

@Service
class GitHubOAuth2UserDatabaseProvider(
    gitHubOAuth2UserService: GitHubOAuth2UserService,
    principalConverter: PrincipalConverter,
    gitHubOAuth2UserConverter: GitHubOAuth2UserConverter
) : AbstractOAuth2UserDatabaseProvider<GitHubOAuth2User>(gitHubOAuth2UserService, principalConverter, gitHubOAuth2UserConverter)