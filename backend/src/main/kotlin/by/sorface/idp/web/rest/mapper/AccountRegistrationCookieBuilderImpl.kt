package by.sorface.idp.web.rest.mapper

import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class AccountRegistrationCookieBuilderImpl : AccountRegistrationCookieBuilder {

    override fun buildId(registrationId: String, maxAge: Int): Cookie {
        // TODO: вынести в переменные окружения

        val cookie = Cookie("registrationId", registrationId)

        cookie.path = "/"
        cookie.maxAge = maxAge
        cookie.domain = "localhost"
        cookie.isHttpOnly = true

        return cookie;
    }

    override fun buildOtpExpiredAt(timestamp: Instant, maxAge: Int): Cookie {
        // TODO: вынести в переменные окружения

        val cookie = Cookie("otp_exp_time", timestamp.toEpochMilli().toString())

        cookie.path = "/"
        cookie.maxAge = maxAge
        cookie.domain = "localhost"
        cookie.isHttpOnly = false

        return cookie;
    }
}