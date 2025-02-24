package by.devpav.kotlin.oidcidp.config.security.oauth2.customizers

import by.devpav.kotlin.oidcidp.config.security.constants.JwtClaims.USER_ROLES_CLAIM_NAME
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer

/**
 * Класс IdTokenCustomizerConfig, который настраивает дополнительную информацию токенов OAuth2.
 */
@Configuration(proxyBeanMethods = false)
class IdTokenCustomizerConfig {

    /**
     * Метод tokenCustomizer, который устанавливает дополнительную информацию токенов OAuth2
     *
     * @return OAuth2TokenCustomizer токенов OAuth2.
     */
    @Bean
    fun tokenCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer { context ->
            when {
                (OidcParameterNames.ID_TOKEN == context.tokenType.value) -> {
                    context.claims.claims { claims ->
                        val userRoles = context.getPrincipal<Authentication>().authorities.map { obj: GrantedAuthority -> obj.authority }.toSet()

                        claims[USER_ROLES_CLAIM_NAME] = userRoles
                    }
                }
            }
        }
    }

}
