package by.devpav.kotlin.oidcidp.config.security.formlogin.records

/**
 * Класс, представляющий учетные данные аккаунта.
 *
 * @property username Имя пользователя.
 * @property password Пароль пользователя.
 */
data class AccountCredentials(var username: String? = "", var password: String? = "")