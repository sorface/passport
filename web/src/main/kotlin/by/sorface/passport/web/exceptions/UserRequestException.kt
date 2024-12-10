package by.sorface.passport.web.exceptions

import org.springframework.http.HttpStatus

class UserRequestException : GlobalSystemException {
    constructor(i18Code: String) : super(i18Code, HttpStatus.BAD_REQUEST)

    constructor(i18Code: String, args: Map<String, String> = HashMap()) : super(i18Code, args, HttpStatus.BAD_REQUEST)
}
