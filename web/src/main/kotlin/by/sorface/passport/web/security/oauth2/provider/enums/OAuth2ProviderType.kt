package by.sorface.passport.web.security.oauth2.provider.enums

enum class OAuth2ProviderType(val socialName: String) {

    GOOGLE("google"),

    GITHUB("github"),

    YANDEX("yandex"),

    TWITCH("twitch"),

    UNKNOWN("unknown");

    companion object {
        fun findByName(value: String?): OAuth2ProviderType =
            if (value == null) UNKNOWN else entries.firstOrNull { it.socialName.equals(value, ignoreCase = true) } ?: UNKNOWN
    }
}