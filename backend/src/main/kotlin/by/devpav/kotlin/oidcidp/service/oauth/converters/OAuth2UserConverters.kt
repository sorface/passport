package by.devpav.kotlin.oidcidp.service.oauth.converters

import by.devpav.kotlin.oidcidp.config.security.oauth2.records.ExternalOAuth2User
import by.devpav.kotlin.oidcidp.utils.UserUtils.parseFullName
import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.core.user.OAuth2User

interface OAuth2UserConverter<T : ExternalOAuth2User> : Converter<OAuth2User, T>

fun <E> Map<String?, E?>.getValueAsStringOrNull(attributeName: String?): String? {
    val value: E? = this[attributeName]

    value ?: return null

    if (value is String) {
        if ("null".equals(value.trim { it <= ' ' }, ignoreCase = true)) {
            return null
        }

        return value
    }

    return value.toString()
}

fun ExternalOAuth2User.addFullName(rowName: String?) {
    rowName ?: return

    val defaultFullName = parseFullName(rowName)

    this.firstName = defaultFullName.firstName
    this.lastName = defaultFullName.lastName
    this.middleName = defaultFullName.otherName
}
