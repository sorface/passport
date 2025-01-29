package by.sorface.passport.web.exceptions

import org.springframework.http.HttpStatus

class NotFoundException : GlobalSystemException {
    constructor(i18Code: String) : super(i18Code, HttpStatus.NOT_FOUND)

    constructor(i18Code: String, args: Map<String, String> = HashMap()) : super(i18Code, args, HttpStatus.NOT_FOUND)
}
