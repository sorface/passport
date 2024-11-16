package by.sorface.passport.web.exceptions

interface SystemException {

    val args: Map<String, String>

    val i18Code: String?

}
