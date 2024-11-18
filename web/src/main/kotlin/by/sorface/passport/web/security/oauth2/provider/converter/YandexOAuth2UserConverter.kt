package by.sorface.passport.web.security.oauth2.provider.converter

import by.sorface.passport.web.constants.claims.YandexClaims
import by.sorface.passport.web.converters.socialusers.OAuth2UserConverter
import by.sorface.passport.web.converters.socialusers.getValueAsStringOrNull
import by.sorface.passport.web.records.socialusers.YandexOAuth2User
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

        if (avatarUrlObject != null) {
            yandexOAuth2User.avatarUrl = avatarUrlObject
        }

        yandexOAuth2User.email = attributes.getValueAsStringOrNull(YandexClaims.CLAIM_DEFAULT_EMAIL)

        return yandexOAuth2User
    }
}