package by.sorface.passport.web.services.users.providers

import by.sorface.passport.web.constants.OAuthProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class DefaultOAuth2UserDatabaseStrategy(
    @Qualifier("gitHubOAuth2UserDatabaseProvider") private val gitHubOAuth2UserSocialOAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
    @Qualifier("yandexOAuth2UserDatabaseProvider") private val yandexOAuth2UserSocialOAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
    @Qualifier("twitchOAuth2UserDatabaseProvider") private val twitchOAuth2UserSocialOAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
    @Qualifier("googleOAuth2UserDatabaseProvider") private val googleOAuth2UserSocialOAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>
) : OAuth2UserDatabaseStrategy {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val provider = getAuthProvider(userRequest)

        val providerService = getUserServiceByProvider(provider)

        return providerService.loadUser(userRequest)
    }

    private fun getUserServiceByProvider(provider: OAuthProvider): OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        return when (provider) {
            OAuthProvider.GITHUB -> gitHubOAuth2UserSocialOAuth2UserService
            OAuthProvider.YANDEX -> yandexOAuth2UserSocialOAuth2UserService
            OAuthProvider.TWITCH -> twitchOAuth2UserSocialOAuth2UserService
            OAuthProvider.GOOGLE -> googleOAuth2UserSocialOAuth2UserService
            else -> throw OAuth2AuthenticationException("Provider %s not supported".formatted(provider))
        }
    }

    private fun getAuthProvider(oAuth2UserRequest: OAuth2UserRequest): OAuthProvider {
        val providerName = oAuth2UserRequest.clientRegistration.clientName

        return OAuthProvider.findByName(providerName)
    }
}
