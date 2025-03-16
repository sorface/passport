package by.sorface.idp.web.rest.controller

import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SpaCsrfController {

    @GetMapping("/api/csrf")
    fun getCsrf(csrfToken: CsrfToken): CsrfToken = csrfToken

}
