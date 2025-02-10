package by.devpav.kotlin.oidcidp.records

import java.util.*

enum class SupportedLocales(val locale: Locale) {
    EN(Locale.ENGLISH),
    RU(Locale.of("ru", "RU"));

    companion object {
        fun getSupportedLocaleOrDefault(language: String, defaultLocale: Locale): Locale {
            return when (language) {
                "ru" -> RU.locale
                "en" -> EN.locale
                else -> defaultLocale
            }
        }
    }

}
