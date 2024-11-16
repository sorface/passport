package by.sorface.passport.web.config.security.jwt

import by.sorface.passport.web.records.principals.DefaultPrincipal
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import java.util.stream.Collectors

@Configuration(proxyBeanMethods = false)
open class IdTokenCustomizerConfig {

    @Bean
    open fun tokenCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer { context: JwtEncodingContext ->
            if (OidcParameterNames.ID_TOKEN == context.tokenType.value || OAuth2TokenType.ACCESS_TOKEN == context.tokenType) {
                context.claims.claims { claims: MutableMap<String?, Any?> -> claims.putAll(buildCustomClaims(context)) }
            }
        }
    }

    private fun buildCustomClaims(context: JwtEncodingContext): Map<String?, Any?> {
        val principal = context.getPrincipal<Authentication>().principal as DefaultPrincipal

        val roles = principal.authorities
            .stream().map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.toSet())

        return java.util.Map.of(CLAIMS_ROLES_NAME, roles, CLAIMS_USER_ID_NAME, principal.id)
    }

    companion object {
        private const val CLAIMS_USER_ID_NAME = "principal-id"

        private const val CLAIMS_ROLES_NAME = "roles"
    }
}
