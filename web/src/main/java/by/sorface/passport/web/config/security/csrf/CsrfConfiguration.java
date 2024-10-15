package by.sorface.passport.web.config.security.csrf;

import by.sorface.passport.web.config.options.CookieOptions;
import by.sorface.passport.web.utils.json.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

/**
 * Configuration class for setting up the CSRF (Cross-Site Request Forgery) protection.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CsrfConfiguration {

    /**
     * Bean method to configure the CookieCsrfTokenRepository.
     *
     * @param cookieOptions The cookie options to be used for configuration.
     * @return The configured CookieCsrfTokenRepository.
     */
    @Bean
    public CookieCsrfTokenRepository cookieCsrfTokenRepository(final CookieOptions cookieOptions) {
        final var cookieCsrfTokenRepository = new CookieCsrfTokenRepository();

        final var csrfCookieOptions = cookieOptions.getCsrf();

        cookieCsrfTokenRepository.setCookiePath(csrfCookieOptions.getPath());
        cookieCsrfTokenRepository.setCookieName(csrfCookieOptions.getName());
        cookieCsrfTokenRepository.setCookieCustomizer(cookieBuilder -> {
            cookieBuilder.httpOnly(csrfCookieOptions.isHttpOnly());
            cookieBuilder.domain(csrfCookieOptions.getDomain());
            cookieBuilder.secure(true);
        });

        log.debug("configured CookieCsrfTokenRepository with settings {}{}", System.lineSeparator(), Json.lazyStringify(csrfCookieOptions));

        return cookieCsrfTokenRepository;
    }

    /**
     * Bean method to configure the CsrfTokenRequestHandler for Single-Page Applications (SPA).
     *
     * @return The configured CsrfTokenRequestHandler for SPA.
     */
    @Bean
    public CsrfTokenRequestHandler spaCsrfTokenRequestHandler() {
        return new SpaCsrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler());
    }

}
