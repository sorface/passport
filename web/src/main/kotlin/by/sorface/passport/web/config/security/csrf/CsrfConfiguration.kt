package by.sorface.passport.web.config.security.csrf

import by.sorface.passport.web.config.options.CookieOptions
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseCookie.ResponseCookieBuilder
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfTokenRequestHandler
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler

@Slf4j
@Configuration
@RequiredArgsConstructor
open class CsrfConfiguration {

    @Bean
    open fun cookieCsrfTokenRepository(cookieOptions: CookieOptions): CookieCsrfTokenRepository {
        val cookieCsrfTokenRepository = CookieCsrfTokenRepository()

        val csrfCookieOptions = cookieOptions.csrf

        cookieCsrfTokenRepository.cookiePath = csrfCookieOptions!!.path
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
