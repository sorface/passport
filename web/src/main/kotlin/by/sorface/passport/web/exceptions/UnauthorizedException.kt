package by.sorface.passport.web.exceptions

import org.springframework.http.HttpStatus

class UnauthorizedException : GlobalSystemException {
    constructor(i18Code: String) : super(i18Code, HttpStatus.UNAUTHORIZED)

    constructor(i18Code: String, args: Map<String, String> = HashMap()) : super(i18Code, args, HttpStatus.UNAUTHORIZED)
}
