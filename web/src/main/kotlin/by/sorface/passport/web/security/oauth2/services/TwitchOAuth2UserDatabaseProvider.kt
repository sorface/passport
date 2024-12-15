package by.sorface.passport.web.security.oauth2.services

import by.sorface.passport.web.security.converters.OAuth2TwitchUserRequestEntityConverter
import by.sorface.passport.web.security.converters.PrincipalConverter
import by.sorface.passport.web.security.oauth2.converter.TwitchOAuth2UserConverter
import by.sorface.passport.web.security.oauth2.records.TwitchOAuth2User
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