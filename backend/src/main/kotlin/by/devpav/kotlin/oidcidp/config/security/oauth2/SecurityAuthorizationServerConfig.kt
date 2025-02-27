package by.devpav.kotlin.oidcidp.config.security.oauth2

import by.devpav.kotlin.oidcidp.config.security.oauth2.properties.OidcAuthorizationProperties
import by.devpav.kotlin.oidcidp.config.web.properties.IdpEndpointProperties
import by.devpav.kotlin.oidcidp.config.security.jose.Jwks
import by.devpav.kotlin.oidcidp.service.oauth.jdbc.DefaultOidcUserInfoService
import com.nimbusds.jose.jwk.JWKSelector
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OidcConfigurer
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OidcUserInfoEndpointConfigurer
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import java.util.function.Function

/**
 * Конфигурация безопасности сервера авторизации.
 */
@Configuration(proxyBeanMethods = true)
class SecurityAuthorizationServerConfig {

    @Autowired
    private lateinit var defaultOidcUserInfoService: DefaultOidcUserInfoService

    @Autowired
    private lateinit var idpEndpointProperties: IdpEndpointProperties

    /**
     * Настройка цепочки фильтров безопасности сервера авторизации.
     *
     * @param http объект HttpSecurity для настройки
     * @return настроенная цепочка фильтров безопасности
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()

        val userInfoMapper = Function<OidcUserInfoAuthenticationContext, OidcUserInfo> { context: OidcUserInfoAuthenticationContext ->
            val authentication = context.getAuthentication<OidcUserInfoAuthenticationToken>()
            val principal = authentication.principal as JwtAuthenticationToken

            defaultOidcUserInfoService.loadUser(principal.name, context.accessToken.scopes)
        }

        return http
            .with(authorizationServerConfigurer) {
                it.oidc { oidc: OidcConfigurer ->
                    oidc.userInfoEndpoint { userInfo: OidcUserInfoEndpointConfigurer ->
                        userInfo.userInfoMapper(
                            userInfoMapper
                        )
                    }
                }
            }
            .securityMatcher(authorizationServerConfigurer.endpointsMatcher)
            .authorizeHttpRequests { authorizeRequests -> authorizeRequests.anyRequest().authenticated() }
            .csrf { csrf: CsrfConfigurer<HttpSecurity?> -> csrf.ignoringRequestMatchers(authorizationServerConfigurer.endpointsMatcher) }
            .oauth2ResourceServer { it.jwt {} }
            .exceptionHandling { exceptions: ExceptionHandlingConfigurer<HttpSecurity?> ->
                exceptions.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint(idpEndpointProperties.loginPage))
            }
            .build()
    }

    /**
     * Настройка параметров сервера авторизации.
     *
     * @param oidcAuthorizationProperties свойства авторизации OIDC
     * @return настроенные параметры сервера авторизации
     */
    @Bean
    fun authorizationServerSettings(oidcAuthorizationProperties: OidcAuthorizationProperties): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder()
            .issuer(oidcAuthorizationProperties.url)
            .build()
    }

    /**
     * Настройка источника JWK.
     *
     * @return источник JWK
     */
    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val rsaKey: RSAKey = Jwks.generateRsa()
        val jwkSet = JWKSet(rsaKey)

        return JWKSource { jwkSelector: JWKSelector, _: SecurityContext? -> jwkSelector.select(jwkSet) }
    }

    /**
     * Настройка декодера JWT.
     *
     * @param jwkSource источник JWK
     * @return декодер JWT
     */
    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext?>): JwtDecoder = OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)

    /**
     * Настройка кодировщика паролей.
     *
     * @return кодировщик паролей
     */
    @Bean
    fun passportEncoder(): PasswordEncoder = BCryptPasswordEncoder(10)

}