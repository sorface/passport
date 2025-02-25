package by.devpav.kotlin.oidcidp.service.oauth.converters

import by.devpav.kotlin.oidcidp.config.security.oauth2.claims.YandexClaims
import by.devpav.kotlin.oidcidp.config.security.oauth2.records.YandexOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class YandexOAuth2UserConverter : OAuth2UserConverter<YandexOAuth2User> {

    override fun convert(oAuth2User: OAuth2User): YandexOAuth2User {
        val attributes = oAuth2User.attributes

        val avatarUrlObject: String? = attributes.getValueAsStringOrNull(YandexClaims.CLAIM_AVATAR_URL)

        val yandexOAuth2User = YandexOAuth2User()

        yandexOAuth2User.id = attributes.getValueAsStringOrNull(YandexClaims.CLAIM_ID)
        yandexOAuth2User.username = attributes.getValueAsStringOrNull(YandexClaims.CLAIM_LOGIN)
        yandexOAuth2User.firstName = attributes.getValueAsStringOrNull(YandexClaims.CLAIM_DEFAULT_FIRST_NAME)
        yandexOAuth2User.lastName = attributes.getValueAsStringOrNull(YandexClaims.CLAIM_DEFAULT_LAST_NAME)
        yandexOAuth2User.email = attributes.getValueAsStringOrNull(YandexClaims.CLAIM_DEFAULT_EMAIL)

        if (avatarUrlObject != null) {
            yandexOAuth2User.avatarUrl = avatarUrlObject
        }

        return yandexOAuth2User
    }

}