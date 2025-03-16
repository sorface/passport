package by.sorface.idp.web.rest.exceptions

import org.springframework.http.HttpStatus

open class I18RestException(
    message: String,
    val i18Code: String = "global.unknown_error",
    val i18Args: Map<String, Any> = mapOf(),
    val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : RuntimeException(message)

