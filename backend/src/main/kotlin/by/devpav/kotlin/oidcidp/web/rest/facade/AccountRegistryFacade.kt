package by.devpav.kotlin.oidcidp.web.rest.facade

import by.devpav.kotlin.oidcidp.web.rest.model.accounts.registration.AccountOtpRefresh
import by.devpav.kotlin.oidcidp.web.rest.model.accounts.registration.AccountRegistration
import by.devpav.kotlin.oidcidp.web.rest.model.accounts.registration.AccountRegistrationResult

/**
 * Интерфейс для работы с регистрацией аккаунтов.
 */
interface AccountRegistryFacade {

    /**
     * Регистрирует новый аккаунт.
     *
     * @param account объект регистрации аккаунта
     * @return результат регистрации аккаунта
     */
    fun registry(account: AccountRegistration): AccountRegistrationResult

    /**
     * Подтверждает регистрацию аккаунта по одноразовому коду.
     *
     * @param registrationId идентификатор регистрации
     * @param otpCode одноразовый код
     * @return true, если подтверждение прошло успешно, иначе false
     */
    fun confirmByOtp(registrationId: String, otpCode: String): Boolean

    /**
     * Обновляет одноразовый код для регистрации аккаунта.
     *
     * @param registrationId идентификатор регистрации
     * @return объект обновления одноразового кода
     */
    fun refreshOtp(registrationId: String): AccountOtpRefresh

}