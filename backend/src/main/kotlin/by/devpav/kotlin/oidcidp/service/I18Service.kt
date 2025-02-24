package by.devpav.kotlin.oidcidp.service

import by.devpav.kotlin.oidcidp.records.I18Codes
import org.springframework.context.i18n.LocaleContextHolder
import java.util.*

interface I18Service {

    fun getI18Message(i18Code: String, args: Map<String, Any> = hashMapOf(), locale: Locale = LocaleContextHolder.getLocale()): String?

    fun getI18MessageOrDefault(
        i18Code: String, args: Map<String, Any> = hashMapOf(), locale: Locale = LocaleContextHolder.getLocale(),
        defaultCode: String = I18Codes.I18GlobalCodes.UNKNOWN_ERROR
    ): String

}

