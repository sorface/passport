package by.devpav.kotlin.oidcidp.web.rest.mapper

import jakarta.servlet.http.Cookie
import java.time.Instant

interface AccountRegistrationCookieBuilder {

    /**
     * Создание cookie для хранения идентификатора временного аккаунта
     *
     * @param registrationId идентификатор временного аккаунта
     */
    fun buildId(registrationId: String): Cookie

    /**
     * Создание cookie для хранения предельного времени действия одноразового пароля (OTP)
     *
     * @param timestamp время, до которого будет действовать OTP
     */
    fun buildOtpExpiredAt(timestamp: Instant): Cookie

}