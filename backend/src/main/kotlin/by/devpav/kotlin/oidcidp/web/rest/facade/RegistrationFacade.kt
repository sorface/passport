package by.devpav.kotlin.oidcidp.web.rest.facade

import by.devpav.kotlin.oidcidp.web.rest.model.accounts.registration.AccountOtpRefresh
import by.devpav.kotlin.oidcidp.web.rest.model.accounts.registration.AccountRegistration
import by.devpav.kotlin.oidcidp.web.rest.model.accounts.registration.AccountRegistrationResult

/**
 * Интерфейс для работы с регистрацией аккаунтов.
 */
interface RegistrationFacade {

    /**
     * Получает информацию о регистрации аккаунта по идентификатору.
     *
     * @param registrationId идентификатор регистрации
     * @return объект регистрации аккаунта
     */
    fun get(registrationId: String): AccountRegistration

    /**
     * Регистрирует новый аккаунт.
     *
     * @param account объект регистрации аккаунта
     * @return результат регистрации аккаунта
     */
    fun create(account: AccountRegistration): AccountRegistrationResult

    /**
     * Подтверждает регистрацию аккаунта по одноразовому коду.
     *
     * @param registrationId идентификатор регистрации
     * @param otpCode одноразовый код
     * @return true, если подтверждение прошло успешно, иначе false
     */
    fun confirm(registrationId: String, otpCode: String)

    /**
     * Обновляет одноразовый код для регистрации аккаунта.
     *
     * @param registrationId идентификатор регистрации
     * @return объект обновления одноразового кода
     */
    fun refreshOtp(registrationId: String): AccountOtpRefresh

}