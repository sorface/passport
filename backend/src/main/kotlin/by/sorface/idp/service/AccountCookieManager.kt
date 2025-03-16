package by.sorface.idp.service

import jakarta.servlet.http.Cookie
import java.time.Instant

interface AccountCookieManager {

    /**
     * Создание cookie для хранения идентификатора временного аккаунта
     *
     * @param registrationId идентификатор временного аккаунта
     */
    fun createRegistrationId(registrationId: String): Cookie

    /**
     * Создание cookie для хранения предельного времени действия одноразового пароля (OTP)
     *
     * @param timestamp время, до которого будет действовать OTP
     */
    fun createOtpExpiredAt(timestamp: Instant): Cookie

    /**
     * Удаление cookie для хранения идентификатора временного аккаунта
     *
     * установка max age = 0
     *
     * @param registrationCookie cookie registrationId
     */
    fun dropRegistrationId(registrationCookie: Cookie): Cookie

    /**
     * Удаление cookie для хранения предельного времени действия одноразового пароля (OTP)
     *
     * установка max age = 0
     *
     * @param otpExpiredAtCookie cookie otpExpiredAtCookie
     */
    fun dropOtpExpiredAt(otpExpiredAtCookie: Cookie): Cookie

}