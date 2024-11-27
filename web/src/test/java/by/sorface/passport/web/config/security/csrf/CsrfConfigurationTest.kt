package by.sorface.passport.web.config.security.csrf

import by.sorface.passport.web.config.options.CsrfCookieOptions
import by.sorface.passport.web.security.config.CsrfConfiguration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CsrfConfigurationTest {
    private val csrfConfiguration = CsrfConfiguration()

    @Test
    fun cookieCsrfTokenRepository() {
        val csrfCookieOptions = CsrfCookieOptions()
        run {
            csrfCookieOptions.domain = "localhost"
            csrfCookieOptions.name = "csrf-cookie-name"
            csrfCookieOptions.httpOnly = true
            csrfCookieOptions.path = "/"
        }

        val cookieCsrfTokenRepository = csrfConfiguration.cookieCsrfTokenRepository(csrfCookieOptions)

        Assertions.assertNotNull(cookieCsrfTokenRepository)
        Assertions.assertEquals(csrfCookieOptions.path, cookieCsrfTokenRepository.cookiePath)
    }
}