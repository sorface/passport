package by.sorface.idp.config.security.jose

import com.nimbusds.jose.jwk.RSAKey

/**
 * Интерфейс [JwksService] предоставляет метод для получения RSA ключа, используемого в процессе работы с JSON Web Keys (JWK).
 *
 * Этот интерфейс используется для инкапсуляции логики получения RSA ключа, что позволяет абстрагироваться от реализации.
 */
interface JwksService {

    /**
     * Возвращает RSA ключ, необходимый для генерации или проверки JWT токенов.
     *
     * @return объект [RSAKey], представляющий RSA ключ
     */
    fun getRsaKey(): RSAKey

}