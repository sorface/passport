package by.sorface.idp.config.web.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.server.Cookie.SameSite
import kotlin.time.Duration

open class IdpCookie(
    var domain: String? = null,
    var path: String? = null,
    var name: String? = null,
    var maxAge: java.time.Duration = java.time.Duration.ofSeconds(3600),
    var httpOnly: Boolean = false,
    var secure: Boolean = true,
    var sameSite: SameSite = SameSite.LAX,
)

open class IdpSessionCookie(
    var domainPattern: String? = null
) : IdpCookie()

@ConfigurationProperties(prefix = "idp.cookie.registration")
class RegistrationCookieProperties : IdpCookie()

@ConfigurationProperties(prefix = "idp.cookie.otpexpat")
class OtpExpAtCookieProperties : IdpCookie()

@ConfigurationProperties(prefix = "idp.cookie.csrf")
class CsrfCookieProperties : IdpCookie()

@ConfigurationProperties(prefix = "idp.cookie.session")
class SessionCookieProperties : IdpSessionCookie()