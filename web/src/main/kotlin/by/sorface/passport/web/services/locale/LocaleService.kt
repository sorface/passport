package by.sorface.passport.web.services.locale

interface LocaleService {

    fun getI18Message(i18Code: String): String?

    fun getI18Message(i18Code: String, args: Map<String, String>): String?

}

