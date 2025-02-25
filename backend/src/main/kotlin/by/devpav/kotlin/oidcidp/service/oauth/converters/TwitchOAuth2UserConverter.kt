package by.devpav.kotlin.oidcidp.service.oauth.converters

import by.devpav.kotlin.oidcidp.config.security.oauth2.claims.TwitchClaims
import by.devpav.kotlin.oidcidp.config.security.oauth2.records.TwitchOAuth2User
import by.devpav.kotlin.oidcidp.web.rest.exceptions.I18RestException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class TwitchOAuth2UserConverter : OAuth2UserConverter<TwitchOAuth2User> {

    override fun convert(oAuth2User: OAuth2User): TwitchOAuth2User {
        val user: Map<String?, String?>? = (oAuth2User.attributes[TwitchClaims.CLAIM_CONTAINER] as ArrayList<Map<String?, String?>?>?)
            ?.stream()
            ?.findFirst()
            ?.orElseThrow { I18RestException("twitch claims invalid") }

        return TwitchOAuth2User().apply {
            id = user?.getValueAsStringOrNull(TwitchClaims.CLAIM_ID)
            username = user?.getValueAsStringOrNull(TwitchClaims.CLAIM_LOGIN)
            email = user?.getValueAsStringOrNull(TwitchClaims.CLAIM_EMAIL)
            avatarUrl = user?.getValueAsStringOrNull(TwitchClaims.CLAIM_PROFILE_IMAGE_URL)
        }
    }

}