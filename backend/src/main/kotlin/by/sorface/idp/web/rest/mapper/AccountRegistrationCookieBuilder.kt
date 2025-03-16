package by.sorface.idp.web.rest.mapper

import jakarta.servlet.http.Cookie
import java.time.Instant

interface AccountRegistrationCookieBuilder {

    /**
     * Создание cookie для хранения идентификатора временного аккаунта
     *
     * @param registrationId идентификатор временного аккаунта
     */
    fun buildId(registrationId: String, maxAge: Int = 600): Cookie

    /**
     * Создание cookie для хранения предельного времени действия одноразового пароля (OTP)
     *
     * @param timestamp время, до которого будет действовать OTP
     */
    fun buildOtpExpiredAt(timestamp: Instant, maxAge: Int = 120): Cookie

}