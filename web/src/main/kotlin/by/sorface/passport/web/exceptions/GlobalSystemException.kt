package by.sorface.passport.web.exceptions

import lombok.Getter
import org.springframework.http.HttpStatus

@Getter
open class GlobalSystemException(override val i18Code: String, val httpStatus: HttpStatus) : RuntimeException(), SystemException {
    final override val args: MutableMap<String, String> = HashMap()

    constructor(i18Code: String, args: Map<String, String> = HashMap(), httpStatus: HttpStatus) : this(i18Code, httpStatus) {
        this.args.putAll(args)
    }
}
