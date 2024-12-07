package by.sorface.passport.web.exceptions

import org.springframework.http.HttpStatus

open class GlobalSystemException(override val i18Code: String, val httpStatus: HttpStatus) : RuntimeException(), SystemException {
    final override val args: MutableMap<String, String> = HashMap()

    constructor(i18Code: String, args: Map<String, String> = mapOf(), httpStatus: HttpStatus) : this(i18Code, httpStatus) {
        this.args.putAll(args)
    }
}
