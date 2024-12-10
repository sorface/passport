package by.sorface.passport.web.exceptions

import org.springframework.http.HttpStatus

class ObjectExpiredException(i18Codes: String) : GlobalSystemException(i18Codes, HttpStatus.BAD_REQUEST)
