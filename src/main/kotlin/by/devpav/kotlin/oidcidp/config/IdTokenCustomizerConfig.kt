package by.devpav.kotlin.oidcidp.config

import by.devpav.kotlin.oidcidp.config.JwtClaims.USER_ROLES_CLAIM_NAME
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import java.util.stream.Collectors

@Configuration(proxyBeanMethods = false)
class IdTokenCustomizerConfig {

    @Bean
    fun tokenCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer { context ->
            when {
                (OidcParameterNames.ID_TOKEN == context.tokenType.value) -> {
                    context.claims.claims { claims ->
                        claims[USER_ROLES_CLAIM_NAME] = context.getPrincipal<Authentication>().authorities
                            .stream()
                            .map { obj: GrantedAuthority -> obj.authority }
                            .collect(Collectors.toSet())
                    }
                }
            }
        }
    }

}
