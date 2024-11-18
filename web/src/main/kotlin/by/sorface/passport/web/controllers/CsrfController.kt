package by.sorface.passport.web.controllers

import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/csrf")
open class CsrfController {
    @GetMapping
    fun getCsrf(csrfToken: CsrfToken): CsrfToken {
        return csrfToken
    }
}
