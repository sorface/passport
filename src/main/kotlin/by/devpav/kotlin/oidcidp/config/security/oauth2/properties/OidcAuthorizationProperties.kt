package by.devpav.kotlin.oidcidp.config.security.oauth2.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Настройка OIDC IdP сервиса
 */
@ConfigurationProperties(prefix = "oidc.idp")
class OidcAuthorizationProperties {

    /**
     * OIDC IdP URL
     */
    var url: String? = null

}