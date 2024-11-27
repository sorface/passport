package by.sorface.passport.web.api

import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/csrf")
class SecurityCsrfController {
    @GetMapping
    fun getCsrf(csrfToken: CsrfToken): CsrfToken = csrfToken
}
