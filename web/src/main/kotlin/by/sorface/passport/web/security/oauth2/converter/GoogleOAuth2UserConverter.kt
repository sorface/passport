package by.sorface.passport.web.security.oauth2.converter

import by.sorface.passport.web.constants.claims.GoogleClaims
import by.sorface.passport.web.security.extensions.OAuth2UserConverter
import by.sorface.passport.web.security.extensions.addFullName
import by.sorface.passport.web.security.extensions.getValueAsStringOrNull
import by.sorface.passport.web.security.oauth2.records.GoogleOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class GoogleOAuth2UserConverter : OAuth2UserConverter<GoogleOAuth2User> {

    override fun convert(oAuth2User: OAuth2User): GoogleOAuth2User {
        val attributes = oAuth2User.attributes

        val googleOAuth2User = GoogleOAuth2User()

        attributes ?: return googleOAuth2User

        googleOAuth2User.id = attributes.getValueAsStringOrNull(GoogleClaims.ATTRIBUTE_SUB)
        googleOAuth2User.avatarUrl = attributes.getValueAsStringOrNull(GoogleClaims.ATTRIBUTE_PICTURE)
        googleOAuth2User.username = attributes.getValueAsStringOrNull(GoogleClaims.ATTRIBUTE_NAME)
        googleOAuth2User.email = attributes.getValueAsStringOrNull(GoogleClaims.ATTRIBUTE_EMAIL)
        googleOAuth2User.addFullName(attributes.getValueAsStringOrNull(GoogleClaims.ATTRIBUTE_NAME))

        return googleOAuth2User
    }
}