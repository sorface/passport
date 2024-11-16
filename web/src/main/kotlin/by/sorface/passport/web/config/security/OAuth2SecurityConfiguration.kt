package by.sorface.passport.web.config.security

import by.sorface.passport.web.config.security.jwt.Jwks.generateRsa
import by.sorface.passport.web.config.security.slo.DelegateLogoutHandler
import by.sorface.passport.web.config.security.slo.OidcPostRedirectLocationLogoutHandler
import com.nimbusds.jose.jwk.JWKSelector
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OidcConfigurer
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OidcLogoutEndpointConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
open class OAuth2SecurityConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Throws(Exception::class)
    open fun authServerSecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity)

        httpSecurity
            .getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc { oidcConfigurer: OidcConfigurer ->
                oidcConfigurer.logoutEndpoint { logoutEndpointConfigurer: OidcLogoutEndpointConfigurer ->
                    val delegateLogoutHandler = DelegateLogoutHandler(
                        CookieClearingLogoutHandler("JSESSIONID"),
                        OidcPostRedirectLocationLogoutHandler()
                    )

                    logoutEndpointConfigurer.logoutResponseHandler(delegateLogoutHandler)
                }
            }

        httpSecurity.exceptionHandling { configurer ->
            configurer.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("http://localhost:3001/account/signin"))
        }

        return httpSecurity.build()
    }

    @Bean
    open fun authorizationServerSettings(): AuthorizationServerSettings = AuthorizationServerSettings.builder().build()

    @Bean
    open fun jwkSource(): JWKSource<SecurityContext> {
        val rsaKey = generateRsa()
        val jwkSet = JWKSet(rsaKey)
        return JWKSource { jwkSelector: JWKSelector, securityContext: SecurityContext? -> jwkSelector.select(jwkSet) }
    }

    @Bean
    open fun jwtDecoder(jwkSource: JWKSource<SecurityContext?>?): JwtDecoder = OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)

    @Bean
    open fun jwtEncoder(jwkSource: JWKSource<SecurityContext?>?): JwtEncoder = NimbusJwtEncoder(jwkSource)

}
