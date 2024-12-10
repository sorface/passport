package by.sorface.passport.web.services.locale

import org.springframework.context.i18n.LocaleContextHolder
import java.util.*

interface LocaleService {

    fun getI18Message(i18Code: String?, args: Map<String, String> = hashMapOf(), locale: Locale = LocaleContextHolder.getLocale()): String?

}

