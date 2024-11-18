package by.sorface.passport.web.security.oauth2.provider.service.database

import by.sorface.passport.web.converters.PrincipalConverter
import by.sorface.passport.web.records.socialusers.GitHubOAuth2User
import by.sorface.passport.web.security.oauth2.provider.converter.GitHubOAuth2UserConverter
import by.sorface.passport.web.services.users.social.GitHubOAuth2UserService
import org.springframework.stereotype.Service

@Service
open class GitHubOAuth2UserDatabaseProvider(
    gitHubOAuth2UserService: GitHubOAuth2UserService,
    principalConverter: PrincipalConverter,
    gitHubOAuth2UserConverter: GitHubOAuth2UserConverter
) : AbstractOAuth2UserDatabaseProvider<GitHubOAuth2User>(gitHubOAuth2UserService, principalConverter, gitHubOAuth2UserConverter)