package by.sorface.sso.web.config.security;

import by.sorface.sso.web.config.options.EndpointOptions;
import by.sorface.sso.web.config.security.handlers.AuthenticationClientErrorHandler;
import by.sorface.sso.web.config.security.handlers.TokenAuthenticationSuccessHandler;
import by.sorface.sso.web.config.security.handlers.TokenRevocationSuccessHandler;
import by.sorface.sso.web.services.redis.RedisOAuth2AuthorizationConsentService;
import by.sorface.sso.web.services.redis.RedisOAuth2AuthorizationService;
import by.sorface.sso.web.services.users.providers.socials.OidcUserDatabaseProvider;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

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
        final var authorizationServerConfigurer = oAuth2AuthorizationServerConfigurer(httpSecurity);

        authorizationServerConfigurer.authorizationEndpoint(configurer -> configurer.errorResponseHandler(authenticationClientErrorHandler));
        authorizationServerConfigurer.tokenRevocationEndpoint(oAuth2TokenRevocationEndpointConfigurer ->
                oAuth2TokenRevocationEndpointConfigurer.revocationResponseHandler(tokenRevocationSuccessHandler));

        final RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        httpSecurity
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(configure -> configure.anyRequest().authenticated())
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(endpointOptions.getUriPageSignIn())))
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> {
                    httpSecurityOAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults());
                })
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .apply(authorizationServerConfigurer);

        return httpSecurity.build();
    }

    /**
     * The authorizationServerSettings function is a Spring Boot
     *
     * @return An authorizationserversettings object
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * The oAuth2AuthorizationServerConfigurer function is a helper function that creates an OAuth2AuthorizationServerConfigurer
     * object and configures it with the appropriate services. The OAuth2AuthorizationServerConfigurer class is used to configure
     * the authorization server, which handles requests for access tokens and authorization codes. It also provides endpoints for
     * checking token validity (the /oauth/introspect endpoint) and revoking tokens (the /oauth/revoke endpoint). The configuration of these endpoints are handled by this function.
     *
     * @param httpSecurity Configure the oauth2authorizationserverconfigurer
     * @return An instance of oauth2authorizationserverconfigurer
     */
    private OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer(final HttpSecurity httpSecurity) {
        final var authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        authorizationServerConfigurer.setBuilder(httpSecurity);
        authorizationServerConfigurer.tokenIntrospectionEndpoint(configurer -> configurer.introspectionResponseHandler(tokenAuthenticationSuccessHandler));
        authorizationServerConfigurer.authorizationService(redisOAuth2AuthorizationService);
        authorizationServerConfigurer.authorizationConsentService(redisOAuth2AuthorizationConsentService);

        authorizationServerConfigurer.oidc(oidcConfigurer -> {
            oidcConfigurer.userInfoEndpoint(oidcUserInfoEndpointConfigurer ->
                    oidcUserInfoEndpointConfigurer.userInfoMapper(oidcUserInfoAuthenticationContext -> {
                        final OAuth2Authorization authorization = oidcUserInfoAuthenticationContext.getAuthorization();
                        return oidcUserDatabaseProvider.loadUser(authorization.getPrincipalName(), authorization.getAuthorizedScopes());
                    }));

            oidcConfigurer.providerConfigurationEndpoint(oidcProviderConfigurationEndpointConfigurer -> {
                oidcProviderConfigurationEndpointConfigurer.providerConfigurationCustomizer(builder -> builder.claim("end_session_endpoint", "http://localhost:8080/logout"));
            });
        });

        return authorizationServerConfigurer;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        final var rsaKey = Jwks.generateRsa();
        final var jwkSet = new JWKSet(rsaKey);

        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    static class Jwks {

        private Jwks() {
        }

        public static RSAKey generateRsa() {
            final KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
            final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
        }
    }

    static class KeyGeneratorUtils {

        private KeyGeneratorUtils() {
        }

        static KeyPair generateRsaKey() {
            KeyPair keyPair;

            try {
                final var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);

                keyPair = keyPairGenerator.generateKeyPair();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }

            return keyPair;
        }
    }
}
