package by.sorface.idp.config.security.oauth2.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Настройка OIDC IdP сервиса
 */
@ConfigurationProperties(prefix = "idp.oidc")
class OidcAuthorizationProperties {

    /**
     * OIDC IdP URL
     */
    var url: String? = null

}