package by.sorface.passport.web.config.security;

import by.sorface.passport.web.config.security.jwt.Jwks;
import by.sorface.passport.web.config.security.slo.DelegateLogoutHandler;
import by.sorface.passport.web.config.security.slo.OidcPostRedirectLocationLogoutHandler;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class OAuth2SecurityConfiguration {

    /**
     * Конфигурируем цепочку фильтров для OAuth 2 и OIDC
     *
     * @param httpSecurity configuring web based security
     * @return security chain
     * @throws Exception исключение ошибки настройки
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        // Устанавливаем настройку сервера OAuth2. Выключаем для них CSRF
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        httpSecurity
                // получаем уже настроенный по умолчанию OAuth2AuthorizationServerConfigurer
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // Включаем OIDC для сервера авторизации. Настройки оставляем по умолчанию
                .oidc(
                        oidcConfigurer -> {
                            oidcConfigurer.logoutEndpoint(logoutEndpointConfigurer -> {
                                final var delegateLogoutHandler = new DelegateLogoutHandler(
                                        new CookieClearingLogoutHandler("JSESSIONID"), // включаем удаление нашей cookie
                                        new OidcPostRedirectLocationLogoutHandler() // перенаправляем на запрошенные post_redirect_url
                                );

                                logoutEndpointConfigurer.logoutResponseHandler(delegateLogoutHandler);
                            });
                        }
                );

        httpSecurity.exceptionHandling(
                configurer -> {
                    // Конфигурируем точку входа в систему. Без данного конфигурирования ты не получишь страницу аутентификации, а 401.
                    configurer.authenticationEntryPoint(
                            // указываем обычный ридерект на страницу входа в систему
                            new LoginUrlAuthenticationEntryPoint("http://localhost:3001/account/signin") // todo заменить редирект на страницу входа
                    );
                }
        );

        return httpSecurity.build();
    }

    /**
     * Настройка URI для нашего OAuth 2 / OIDC.
     *
     * @return объект содержащий набор URI для /oauth2 (oidc)
     */
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

    /**
     * JWT декодер, будет применяться для получения
     *
     * @param jwkSource откуда получаем данные о сгенеренных парах jwkSource.
     * @return декодер JWT токенов
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public JwtEncoder jwtEncoder(final JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

}
