package by.sorface.idp.service.impl

import by.sorface.idp.config.web.properties.OtpExpAtCookieProperties
import by.sorface.idp.config.web.properties.RegistrationCookieProperties
import by.sorface.idp.service.AccountCookieManager
import jakarta.servlet.http.Cookie
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class AccountCookieManagerImpl(
    val otpExpAtCookieProperties: OtpExpAtCookieProperties,
    val registrationCookieProperties: RegistrationCookieProperties
) : AccountCookieManager {

    private val logger = LoggerFactory.getLogger(AccountCookieManagerImpl::class.java)

    override fun createRegistrationId(registrationId: String): Cookie {
        logger.info("creating registration cookie with registrationId [{}]", registrationId)

        return Cookie(registrationCookieProperties.name, registrationId).apply {
            domain = registrationCookieProperties.domain
            path = registrationCookieProperties.path
            maxAge = registrationCookieProperties.maxAge.toSeconds().toInt()
            isHttpOnly = registrationCookieProperties.httpOnly
            secure = registrationCookieProperties.secure
        }
    }

    override fun createOtpExpiredAt(timestamp: Instant): Cookie {
        logger.info("creating otp expired cookie [{}]", timestamp)

        return Cookie(otpExpAtCookieProperties.name, timestamp.toEpochMilli().toString()).apply {
            domain = otpExpAtCookieProperties.domain
            path = otpExpAtCookieProperties.path
            maxAge = otpExpAtCookieProperties.maxAge.toSeconds().toInt()
            isHttpOnly = true
            secure = otpExpAtCookieProperties.secure
        }
    }

    override fun dropRegistrationId(registrationCookie: Cookie): Cookie {
        logger.info("drop registration id cookie [{}]", registrationCookie.value)

        return registrationCookie.apply {
            maxAge = 0
            path = registrationCookieProperties.path
            isHttpOnly = registrationCookieProperties.httpOnly
        }
    }

    override fun dropOtpExpiredAt(otpExpiredAtCookie: Cookie): Cookie {
        logger.info("drop otp expired at [{}] cookie", otpExpiredAtCookie.value)

        return otpExpiredAtCookie.apply {
            maxAge = 0
            path = otpExpAtCookieProperties.path
            isHttpOnly = otpExpAtCookieProperties.httpOnly
        }
    }

}