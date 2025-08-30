package by.sorface.idp.config.security.formlogin.records

/**
 * Данные об успешной аутентификации аккаунта.
 *
 * @property redirectUrl URL, на который будет перенаправлен пользователь после успешного входа
 */
data class AccountSuccessAuthentication(
    /**
     * URL-адрес для перенаправления после успешной аутентификации.
     */
    val redirectUrl: String
)