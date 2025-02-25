package by.devpav.kotlin.oidcidp.service.oauth.providers

import by.devpav.kotlin.oidcidp.config.security.oauth2.records.TwitchOAuth2User
import by.devpav.kotlin.oidcidp.service.oauth.jdbc.TwitchOAuth2UserService
import by.devpav.kotlin.oidcidp.service.oauth.converters.OAuth2TwitchUserRequestEntityConverter
import by.devpav.kotlin.oidcidp.service.oauth.converters.PrincipalConverter
import by.devpav.kotlin.oidcidp.service.oauth.converters.TwitchOAuth2UserConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TwitchOAuth2UserDatabaseProvider @Autowired constructor(
    twitchOAuth2UserService: TwitchOAuth2UserService,
    principalConverter: PrincipalConverter,
    twitchOAuth2UserConverter: TwitchOAuth2UserConverter
) : AbstractOAuth2UserDatabaseProvider<TwitchOAuth2User>(twitchOAuth2UserService, principalConverter, twitchOAuth2UserConverter) {

    init {
        super.setRequestEntityConverter(TWITCH_REQUEST_CONVERTER)
    }

    companion object {
        val TWITCH_REQUEST_CONVERTER: OAuth2TwitchUserRequestEntityConverter = OAuth2TwitchUserRequestEntityConverter()
    }
}