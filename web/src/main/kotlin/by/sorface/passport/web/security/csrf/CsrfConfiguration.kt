package by.sorface.passport.web.security.csrf

import by.sorface.passport.web.config.options.CookieProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseCookie.ResponseCookieBuilder
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfTokenRequestHandler
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler

@Configuration
open class CsrfConfiguration {

    @Bean
    open fun cookieCsrfTokenRepository(cookieProperties: CookieProperties): CookieCsrfTokenRepository {
        val cookieCsrfTokenRepository = CookieCsrfTokenRepository()

        val csrfCookieOptions = cookieProperties.csrf

        cookieCsrfTokenRepository.cookiePath = csrfCookieOptions.path
        cookieCsrfTokenRepository.setCookieName(csrfCookieOptions.name)

        cookieCsrfTokenRepository.setCookieCustomizer { cookieBuilder: ResponseCookieBuilder ->
            cookieBuilder.httpOnly(csrfCookieOptions.httpOnly)
            cookieBuilder.domain(csrfCookieOptions.domain)
            cookieBuilder.secure(true)
        }

        return cookieCsrfTokenRepository
    }

    @Bean
    open fun spaCsrfTokenRequestHandler(): CsrfTokenRequestHandler {
        return SpaCsrfTokenRequestHandler(XorCsrfTokenRequestAttributeHandler())
    }
}
