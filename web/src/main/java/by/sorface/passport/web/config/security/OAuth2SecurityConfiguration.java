package by.sorface.passport.web.config.security;

import by.sorface.passport.web.config.options.EndpointOptions;
import by.sorface.passport.web.config.security.handlers.AuthenticationClientErrorHandler;
import by.sorface.passport.web.config.security.handlers.TokenAuthenticationSuccessHandler;
import by.sorface.passport.web.config.security.handlers.TokenRevocationSuccessHandler;
import by.sorface.passport.web.config.security.jwt.Jwks;
import by.sorface.passport.web.services.redis.RedisOAuth2AuthorizationConsentService;
import by.sorface.passport.web.services.redis.RedisOAuth2AuthorizationService;
import by.sorface.passport.web.services.users.providers.socials.OidcUserDatabaseProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class OAuth2SecurityConfiguration {

    private final RedisOAuth2AuthorizationService redisOAuth2AuthorizationService;

    private final RedisOAuth2AuthorizationConsentService redisOAuth2AuthorizationConsentService;

    private final TokenAuthenticationSuccessHandler tokenAuthenticationSuccessHandler;

    private final AuthenticationClientErrorHandler authenticationClientErrorHandler;

    private final EndpointOptions endpointOptions;

    private final TokenRevocationSuccessHandler tokenRevocationSuccessHandler;

    private final OidcUserDatabaseProvider oidcUserDatabaseProvider;

    /**
     * Configuration OAuth2 Spring Security
     *
     * @param httpSecurity configuring web based security
     * @return security chain
     * @throws Exception исключение ошибки настройки
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        final OAuth2AuthorizationServerConfigurer authorizationServerConfigure = oAuth2AuthorizationServerConfigurer(httpSecurity);

        httpSecurity.with(authorizationServerConfigure, configure -> {
            configure.tokenIntrospectionEndpoint(configurer -> {
                configurer.introspectionResponseHandler(tokenAuthenticationSuccessHandler);
            });
            configure.authorizationEndpoint(configurer -> {
                configurer.errorResponseHandler(authenticationClientErrorHandler);
            });
            configure.tokenRevocationEndpoint(oAuth2TokenRevocationEndpointConfigurer -> {
                oAuth2TokenRevocationEndpointConfigurer.revocationResponseHandler(tokenRevocationSuccessHandler);
            });
            configure.tokenEndpoint(oAuth2TokenEndpointConfigurer -> {
                oAuth2TokenEndpointConfigurer.errorResponseHandler((request, response, exception) -> {
                    if (exception instanceof OAuth2AuthenticationException oAuth2AuthenticationException) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                        try (var writer = response.getWriter()) {
                            final var objectMapper = new ObjectMapper();

                            String jsonContent = objectMapper.writeValueAsString(
                                    Map.of(
                                            "code", HttpServletResponse.SC_UNAUTHORIZED,
                                            "description", oAuth2AuthenticationException.getError().getErrorCode()
                                    )
                            );
                            writer.write(jsonContent);
                        } catch (IOException ignored) {
                        }
                    }
                });
            });

            configure.authorizationService(redisOAuth2AuthorizationService);
            configure.authorizationConsentService(redisOAuth2AuthorizationConsentService);
            configure.oidc(oidcConfigurer -> {
                oidcConfigurer.userInfoEndpoint(oidcUserInfoEndpointConfigurer ->
                        oidcUserInfoEndpointConfigurer.userInfoMapper(oidcUserInfoAuthenticationContext -> {
                            final OAuth2Authorization authorization = oidcUserInfoAuthenticationContext.getAuthorization();
                            return oidcUserDatabaseProvider.loadUser(authorization.getPrincipalName(), authorization.getAuthorizedScopes());
                        }));
                oidcConfigurer.providerConfigurationEndpoint(oidcProviderConfigurationEndpointConfigurer -> {
                    oidcProviderConfigurationEndpointConfigurer.providerConfigurationCustomizer(builder -> builder.claim("end_session_endpoint", "http://localhost:8080/logout"));
                });
            });
        });

        final RequestMatcher endpointsMatcher = authorizationServerConfigure.getEndpointsMatcher();

        httpSecurity
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(configure -> configure.anyRequest().authenticated())
                .exceptionHandling(configure -> configure.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(endpointOptions.getUriPageSignIn())))
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> {
                    httpSecurityOAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults());
                })
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        final var rsaKey = Jwks.generateRsa();
        final var jwkSet = new JWKSet(rsaKey);

        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer(final HttpSecurity httpSecurity) {
        final var authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        authorizationServerConfigurer.setBuilder(httpSecurity);

        return authorizationServerConfigurer;
    }
}
