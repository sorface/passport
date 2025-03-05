package by.devpav.kotlin.oidcidp.web.rest.mapper

import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class AccountRegistrationCookieBuilderImpl : AccountRegistrationCookieBuilder {

    override fun buildId(registrationId: String): Cookie {
        // TODO: вынести в переменные окружения

        val cookie = Cookie("registrationId", registrationId)

        cookie.path = "/"
        cookie.maxAge = 600
        cookie.domain = "localhost"
        cookie.isHttpOnly = true

        return cookie;
    }

    override fun buildOtpExpiredAt(timestamp: Instant): Cookie {
        // TODO: вынести в переменные окружения

        val cookie = Cookie("otp_exp_time", timestamp.toEpochMilli().toString())

        cookie.path = "/"
        cookie.maxAge = 120
        cookie.domain = "localhost"
        cookie.isHttpOnly = false

        return cookie;
    }
}