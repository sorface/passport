package by.sorface.idp.config.security

import by.sorface.idp.config.security.csrf.CsrfCookieFilter
import by.sorface.idp.config.security.csrf.SpaCsrfTokenRequestHandler
import by.sorface.idp.config.security.entrypoints.JsonUnauthorizedAuthenticationEntryPoint
import by.sorface.idp.config.security.formlogin.JsonFormLoginFailureHandler
import by.sorface.idp.config.security.formlogin.SessionRedirectSuccessHandler
import by.sorface.idp.config.security.oauth2.CustomLogoutHandler
import by.sorface.idp.config.web.properties.CsrfCookieProperties
import by.sorface.idp.config.web.properties.IdpEndpointProperties
import by.sorface.idp.config.web.properties.SessionCookieProperties
import by.sorface.idp.extencions.jsonLogin
import by.sorface.idp.service.oauth.OAuth2UserDatabaseStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseCookie.ResponseCookieBuilder
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy
import org.springframework.security.web.csrf.CsrfTokenRequestHandler
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.session.web.http.CookieSerializer
import org.springframework.session.web.http.DefaultCookieSerializer
import org.springframework.web.cors.CorsConfigurationSource

@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug = true)
@Profile("development")
class SecurityDevelopmentConfig {

    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests.requestMatchers("/h2-console/**", "**.css", "**.js", "**,jsp", "/graphiql/**", "/graphql/**").permitAll()
                    .anyRequest().authenticated()
            }
            .csrf { csrf -> csrf.disable() }
            .cors { cors -> cors.disable() }
            .formLogin(Customizer.withDefaults())

        return http.build()
    }

}

/**
 * Конфигурация безопасности для production и песочницы
 */
@EnableMethodSecurity
@Configuration(proxyBeanMethods = true)
@EnableWebSecurity
@Profile("production", "sandbox")
class SecurityProductionConfig {

    @Autowired
    private lateinit var jsonUnauthorizedAuthenticationEntryPoint: JsonUnauthorizedAuthenticationEntryPoint

    @Autowired
    private lateinit var csrfCookieProperties: CsrfCookieProperties

    @Autowired
    private lateinit var corsConfigurationSource: CorsConfigurationSource

    @Autowired
    private lateinit var sessionRedirectSuccessHandler: SessionRedirectSuccessHandler

    @Autowired
    private lateinit var jsonFormLoginFailureHandler: JsonFormLoginFailureHandler

    @Autowired
    private lateinit var idpEndpointProperties: IdpEndpointProperties

    @Autowired
    private lateinit var oAuth2UserDatabaseStrategy: OAuth2UserDatabaseStrategy

    @Autowired
    private lateinit var customLogoutHandler: CustomLogoutHandler

    /**
     * Создает цепочку фильтров безопасности по умолчанию
     *
     * @param http Объект HttpSecurity для настройки безопасности.
     * @return Объект SecurityFilterChain, представляющий цепочку фильтров безопасности.
     */
    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .oauth2Login { oauth2LoginSpec ->
                oauth2LoginSpec.userInfoEndpoint { userInfoEndpointSpec ->
                    userInfoEndpointSpec.userService(oAuth2UserDatabaseStrategy)
                }
                oauth2LoginSpec.loginPage(idpEndpointProperties.loginPage)
                oauth2LoginSpec.successHandler(sessionRedirectSuccessHandler)
                oauth2LoginSpec.failureHandler(jsonFormLoginFailureHandler)
            }
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/h2-console/**", "**.css", "**.js", "**,jsp", "/graphiql/**", "/graphql/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/csrf")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/registrations").anonymous()
                    .requestMatchers(HttpMethod.POST, "/api/registrations").anonymous()
                    .requestMatchers(HttpMethod.PUT, "/api/registrations/otp").anonymous()
                    .requestMatchers(HttpMethod.POST, "/api/registrations/confirm").anonymous()
                    .requestMatchers(HttpMethod.GET, "/api/accounts/login/{login}/exists").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/accounts/authenticated").permitAll()
                    .requestMatchers(HttpMethod.POST, idpEndpointProperties.loginPath).anonymous()
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().permitAll()
            }
            .requestCache { httpSecurityRequestCacheConfigurer: RequestCacheConfigurer<HttpSecurity?> ->
                val httpSessionRequestCache = HttpSessionRequestCache()

                httpSessionRequestCache.setRequestMatcher(AntPathRequestMatcher("/oauth2/**"))
                httpSecurityRequestCacheConfigurer.requestCache(httpSessionRequestCache)
            }
            .logout { logoutSpec ->
                logoutSpec.addLogoutHandler(customLogoutHandler)
            }
            .csrf { csrfSpec -> csrfConfigurer(csrfSpec) }
            .addFilterAfter(CsrfCookieFilter(), BasicAuthenticationFilter::class.java) // csrf filter for SPA
            .cors { cors -> cors.configurationSource(corsConfigurationSource) }
            .jsonLogin { jsonLoginSpec ->
                jsonLoginSpec.loginPage(idpEndpointProperties.loginPage)
                jsonLoginSpec.loginProcessingUrl(idpEndpointProperties.loginPath)
                jsonLoginSpec.successHandler(sessionRedirectSuccessHandler)
                jsonLoginSpec.failureHandler(jsonFormLoginFailureHandler)
            }
            .exceptionHandling { exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(jsonUnauthorizedAuthenticationEntryPoint)
            }

        return http.build()
    }

    /**
     * Настройка конфигурации CSRF
     *
     * @param csrfConfigurer Объект CsrfConfigurer для настройки CSRF.
     */
    private fun csrfConfigurer(csrfConfigurer: CsrfConfigurer<HttpSecurity>) {
        val cookieCsrfTokenRepository = getCookieCsrfTokenRepository(csrfCookieProperties)
        val csrfAuthenticationStrategy = CsrfAuthenticationStrategy(cookieCsrfTokenRepository)
        val spaCsrfTokenRequestHandler = SpaCsrfTokenRequestHandler(XorCsrfTokenRequestAttributeHandler())

        csrfConfigurer
            .ignoringRequestMatchers(
                AntPathRequestMatcher.antMatcher(HttpMethod.GET),
                AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS)
            )
            .csrfTokenRepository(cookieCsrfTokenRepository)
            .csrfTokenRequestHandler(spaCsrfTokenRequestHandler)
            .sessionAuthenticationStrategy(csrfAuthenticationStrategy)
    }

    @Configuration
    class CookieConfiguration {
        @Bean
        fun sessionCookieSerializer(sessionCookieProperties: SessionCookieProperties): CookieSerializer {
            return sessionCookieProperties.run {
                val serializer = DefaultCookieSerializer()

                serializer.setCookieName(this.name)
                serializer.setCookiePath(this.path)
                serializer.setDomainNamePattern(this.domainPattern)
                serializer.setUseHttpOnlyCookie(this.httpOnly)
                serializer.setSameSite(sessionCookieProperties.sameSite.name)
                serializer.setCookieMaxAge(sessionMaxAge.toSeconds().toInt())

                serializer
            }
        }
    }

    /**
     * Создает репозиторий CSRF-токенов на основе свойств CSRF.
     *
     * @param csrfCookieProperties Объект CsrfCookieProperties, содержащий свойства CSRF.
     * @return Объект CookieCsrfTokenRepository, представляющий репозиторий CSRF-токенов.
     */
    private fun getCookieCsrfTokenRepository(csrfCookieProperties: CsrfCookieProperties): CookieCsrfTokenRepository {
        val cookieCsrfTokenRepository = CookieCsrfTokenRepository()

        cookieCsrfTokenRepository.setCookieName(csrfCookieProperties.name)
        cookieCsrfTokenRepository.cookiePath = csrfCookieProperties.path

        cookieCsrfTokenRepository.setCookieCustomizer { cookieBuilder: ResponseCookieBuilder ->
            cookieBuilder.httpOnly(csrfCookieProperties.httpOnly)
            cookieBuilder.domain(csrfCookieProperties.domain)
            cookieBuilder.secure(csrfCookieProperties.secure)
        }

        return cookieCsrfTokenRepository
    }

    /**
     * Создает обработчик запросов CSRF для SPA
     *
     * @return Объект CsrfTokenRequestHandler, представляющий обработчик запросов CSRF для SPA.
     */
    @Bean
    fun spaCsrfTokenRequestHandler(): CsrfTokenRequestHandler {
        return SpaCsrfTokenRequestHandler(XorCsrfTokenRequestAttributeHandler())
    }

}
