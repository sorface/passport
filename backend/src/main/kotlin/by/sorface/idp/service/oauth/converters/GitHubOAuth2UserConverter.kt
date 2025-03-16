package by.sorface.idp.service.oauth.converters

import by.sorface.idp.config.security.oauth2.claims.GitHubClaims
import by.sorface.idp.config.security.oauth2.records.GitHubOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class GitHubOAuth2UserConverter : OAuth2UserConverter<GitHubOAuth2User> {

    override fun convert(oAuth2User: OAuth2User): GitHubOAuth2User {
        val attributes = oAuth2User.attributes

        val id = attributes.getValueAsStringOrNull(GitHubClaims.CLAIM_ID)
        val avatarUrl = attributes.getValueAsStringOrNull(GitHubClaims.CLAIM_AVATAR_URL)
        val login = attributes.getValueAsStringOrNull(GitHubClaims.CLAIM_LOGIN)
        val name = attributes.getValueAsStringOrNull(GitHubClaims.CLAIM_NAME)
        val email = attributes.getValueAsStringOrNull(GitHubClaims.CLAIM_EMAIL)

        val gitHubOAuth2User = GitHubOAuth2User()

        gitHubOAuth2User.id = id
        gitHubOAuth2User.avatarUrl = avatarUrl
        gitHubOAuth2User.username = login
        gitHubOAuth2User.email = email
        gitHubOAuth2User.addFullName(name)

        return gitHubOAuth2User
    }
}