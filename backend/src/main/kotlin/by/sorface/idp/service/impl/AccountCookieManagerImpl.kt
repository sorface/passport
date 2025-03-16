package by.sorface.idp.service.impl

import by.sorface.idp.config.web.properties.OtpExpAtCookieProperties
import by.sorface.idp.config.web.properties.RegistrationCookieProperties
import by.sorface.idp.service.AccountCookieManager
import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class AccountCookieManagerImpl(
    val otpExpAtCookieProperties: OtpExpAtCookieProperties,
    val registrationCookieProperties: RegistrationCookieProperties
) : AccountCookieManager {

    override fun createRegistrationId(registrationId: String): Cookie {
        return Cookie(registrationCookieProperties.name, registrationId).apply {
            domain = registrationCookieProperties.domain
            path = registrationCookieProperties.path
            maxAge = registrationCookieProperties.maxAge
            isHttpOnly = registrationCookieProperties.httpOnly
            secure = registrationCookieProperties.secure
        }
    }

    override fun createOtpExpiredAt(timestamp: Instant): Cookie {
        return Cookie(otpExpAtCookieProperties.name, timestamp.toEpochMilli().toString()).apply {
            domain = otpExpAtCookieProperties.domain
            path = otpExpAtCookieProperties.path
            maxAge = otpExpAtCookieProperties.maxAge
            isHttpOnly = true
            secure = otpExpAtCookieProperties.secure
        }
    }

    override fun dropRegistrationId(registrationCookie: Cookie): Cookie {
        return registrationCookie.apply {
            maxAge = 0
            path = registrationCookieProperties.path
            isHttpOnly = registrationCookieProperties.httpOnly
        }
    }

    override fun dropOtpExpiredAt(otpExpiredAtCookie: Cookie): Cookie {
        return otpExpiredAtCookie.apply {
            maxAge = 0
            path = otpExpAtCookieProperties.path
            isHttpOnly = otpExpAtCookieProperties.httpOnly
        }
    }

}