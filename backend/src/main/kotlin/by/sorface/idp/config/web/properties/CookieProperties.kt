package by.sorface.idp.config.web.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.server.Cookie.SameSite
import kotlin.time.Duration

open class IdpCookie(
    var domain: String? = null,
    var path: String? = null,
    var name: String? = null,
    var maxAge: Int = 3600,
    var httpOnly: Boolean = false,
    var secure: Boolean = true
)

open class IdpSessionCookie(
    var domainPattern: String? = null,
    var sameSite: SameSite = SameSite.NONE,
    var sessionMaxAge: java.time.Duration = java.time.Duration.ofDays(365)
) : IdpCookie()

@ConfigurationProperties(prefix = "idp.cookie.registration")
class RegistrationCookieProperties : IdpCookie()

@ConfigurationProperties(prefix = "idp.cookie.otpexpat")
class OtpExpAtCookieProperties : IdpCookie()

@ConfigurationProperties(prefix = "idp.cookie.csrf")
class CsrfCookieProperties : IdpCookie()

@ConfigurationProperties(prefix = "idp.cookie.session")
class SessionCookieProperties : IdpSessionCookie()