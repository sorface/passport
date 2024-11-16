package by.sorface.passport.web.config.options

import org.apache.tomcat.util.http.SameSiteCookies
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("sorface.cookie")
open class CookieOptions {
    /**
     * The session cookie options.
     */
    var session: SessionCookieOptions? = null

    /**
     * The session cookie options.
     */
    var csrf: CsrfCookieOptions? = null

    open class SessionCookieOptions {
        /**
         * The session cookie options.
         */
        var domainPattern: String? = null

        /**
         * The path for the session cookie.
         */
        var path: String? = null

        /**
         * The path for the session cookie.
         */
        var name: String? = null

        /**
         * The path for the session cookie.
         */
        var sameSite: SameSiteCookies? = null

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
