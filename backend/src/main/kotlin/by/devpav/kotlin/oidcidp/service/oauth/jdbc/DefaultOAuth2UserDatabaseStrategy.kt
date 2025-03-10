package by.devpav.kotlin.oidcidp.service.oauth.jdbc

import by.devpav.kotlin.oidcidp.service.oauth.OAuth2UserDatabaseStrategy
import by.devpav.kotlin.oidcidp.config.security.oauth2.enums.OAuth2ProviderType
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
    @Qualifier("twitchOAuth2UserDatabaseProvider") private val twitchOAuth2UserSocialOAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>
) : OAuth2UserDatabaseStrategy {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val provider = getAuthProvider(userRequest)

        val providerService = getUserServiceByProvider(provider)

        return providerService.loadUser(userRequest)
    }

    private fun getUserServiceByProvider(provider: OAuth2ProviderType): OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        return when (provider) {
            OAuth2ProviderType.GITHUB -> gitHubOAuth2UserSocialOAuth2UserService
            OAuth2ProviderType.YANDEX -> yandexOAuth2UserSocialOAuth2UserService
            OAuth2ProviderType.TWITCH -> twitchOAuth2UserSocialOAuth2UserService

            else -> throw OAuth2AuthenticationException("provider $provider not supported")
        }
    }

    private fun getAuthProvider(oAuth2UserRequest: OAuth2UserRequest): OAuth2ProviderType {
        val providerName = oAuth2UserRequest.clientRegistration.clientName

        return OAuth2ProviderType.findByName(providerName)
    }
}
