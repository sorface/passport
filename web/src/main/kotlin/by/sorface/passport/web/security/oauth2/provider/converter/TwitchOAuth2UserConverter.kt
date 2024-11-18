package by.sorface.passport.web.security.oauth2.provider.converter

import by.sorface.passport.web.constants.claims.TwitchClaims
import by.sorface.passport.web.converters.socialusers.OAuth2UserConverter
import by.sorface.passport.web.converters.socialusers.getValueAsStringOrNull
import by.sorface.passport.web.exceptions.ObjectInvalidException
import by.sorface.passport.web.records.socialusers.TwitchOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class TwitchOAuth2UserConverter : OAuth2UserConverter<TwitchOAuth2User> {

    override fun convert(oAuth2User: OAuth2User): TwitchOAuth2User {
        val user: Map<String?, String?>? = (oAuth2User.attributes[TwitchClaims.CLAIM_CONTAINER] as ArrayList<Map<String?, String?>?>?)
            ?.stream()
            ?.findFirst()
            ?.orElseThrow { ObjectInvalidException("twitch claims invalid") }

        val twitchOAuth2User = TwitchOAuth2User()

        twitchOAuth2User.id = user?.getValueAsStringOrNull(TwitchClaims.CLAIM_ID)
        twitchOAuth2User.username = user?.getValueAsStringOrNull(TwitchClaims.CLAIM_LOGIN)
        twitchOAuth2User.email = user?.getValueAsStringOrNull(TwitchClaims.CLAIM_EMAIL)
        twitchOAuth2User.avatarUrl = user?.getValueAsStringOrNull(TwitchClaims.CLAIM_PROFILE_IMAGE_URL)

        return twitchOAuth2User
    }

}