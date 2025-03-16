package by.sorface.idp.config.web.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Класс, содержащий свойства конечной точки Idp.
 * Свойства конфигурируются с префиксом "idp.endpoint".
 */
@ConfigurationProperties(prefix = "idp.endpoint")
class IdpEndpointProperties {

    /**
     * Путь для входа в систему.
     * По умолчанию установлено значение "/api/login".
     */
    var loginPath: String? = "/api/login"

    /**
     * Страница входа в систему.
     * По умолчанию установлено значение "/".
     */
    var loginPage: String? = "/"
}

/**
 * Класс, содержащий свойства конечной точки Idp для фронтенда.
 * Свойства конфигурируются с префиксом "idp.endpoint.frontend".
 */
@ConfigurationProperties(prefix = "idp.endpoint.frontend")
class IdpFrontendEndpointProperties {

    /**
     * Страница учетной записи.
     * По умолчанию установлено значение "http://localhost:9020".
     */
    var accountPage: String? = "http://localhost:9020"

}