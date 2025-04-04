package by.sorface.idp.config.security.backchannel.generator

import by.sorface.idp.config.security.backchannel.context.OidcContext
import org.springframework.security.oauth2.jwt.Jwt

/**
 * Интерфейс представляет генератор токенов для выхода из системы по протоколу OIDC.
 *
 * @param T тип токена, который будет генерироваться. Должен быть подтипом Jwt.
 */
interface OidcLogoutTokenGenerator<T : Jwt> {

    /**
     * Метод generate генерирует токен для выхода из системы по протоколу OIDC.
     * @param context контекст выхода из системы, содержащий информацию о клиенте и токене.
     * @return сгенерированный токен.
     */
    fun generate(context: OidcContext): T

}