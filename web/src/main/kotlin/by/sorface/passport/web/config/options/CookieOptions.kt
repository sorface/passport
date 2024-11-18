package by.sorface.passport.web.config.options

import org.apache.tomcat.util.http.SameSiteCookies
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("sorface.cookie")
open class CookieOptions {

    val session: SessionCookieOptions = SessionCookieOptions()

    var csrf: CsrfCookieOptions = CsrfCookieOptions()

    open class SessionCookieOptions {

        var domainPattern: String? = null

        var path: String? = null

        var name: String? = null

        /**
         * The path for the session cookie.
         */
        lateinit var sameSite: SameSiteCookies

        /**
         * Whether the session cookie is only accessible via HTTP.
         */
        var httpOnly = false
    }

    open class CsrfCookieOptions {
        /**
         * The domain for the CSRF cookie.
         */
        var domain: String? = null

        /**
         * The domain for the CSRF cookie.
         */
        var path: String? = null

        /**
         * The domain for the CSRF cookie.
         */
        var name: String? = null

        /**
         * Whether the CSRF cookie is only accessible via HTTP.
         */
        var httpOnly = false
    }
}
