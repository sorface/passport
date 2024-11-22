package by.sorface.passport.web.security.oauth2.config

import by.sorface.passport.web.config.options.CookieProperties
import by.sorface.passport.web.security.oauth2.jwt.Jwks.generateRsa
import by.sorface.passport.web.security.oauth2.slo.DelegateLogoutHandler
import by.sorface.passport.web.security.oauth2.slo.OidcPostRedirectLocationLogoutHandler
import by.sorface.passport.web.security.oauth2.slo.OidcWebSessionLogoutHandler
import by.sorface.passport.web.security.sessions.AccountSessionService
import com.nimbusds.jose.jwk.JWKSelector
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OidcConfigurer
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OidcLogoutEndpointConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler

@Configuration
@EnableWebSecurity(debug = true)
class OAuth2SecurityConfiguration(
    private val accountSessionService: AccountSessionService,
    private val authorizationService: OAuth2AuthorizationService
) {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Throws(Exception::class)
    fun authServerSecurityFilterChain(httpSecurity: HttpSecurity, cookieProperties: CookieProperties): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity)

        httpSecurity
            .getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc { oidcConfigurer: OidcConfigurer ->
                oidcConfigurer.logoutEndpoint { logoutEndpointConfigurer: OidcLogoutEndpointConfigurer ->
                    val delegateLogoutHandler = DelegateLogoutHandler(
                        CookieClearingLogoutHandler(cookieProperties.session.name, cookieProperties.csrf.name),
                        OidcWebSessionLogoutHandler(accountSessionService, authorizationService),
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
    fun authorizationServerSettings(): AuthorizationServerSettings = AuthorizationServerSettings.builder().build()

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val rsa = generateRsa()

        val jwkSet = JWKSet(rsa)

        return JWKSource { jwkSelector: JWKSelector, _ -> jwkSelector.select(jwkSet) }
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext?>?): JwtDecoder = OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)

    @Bean
    fun jwtEncoder(jwkSource: JWKSource<SecurityContext?>?): JwtEncoder = NimbusJwtEncoder(jwkSource)

}
