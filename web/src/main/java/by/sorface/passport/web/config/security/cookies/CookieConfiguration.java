package by.sorface.passport.web.config.security.cookies;

import by.sorface.passport.web.config.options.CookieOptions;
import by.sorface.passport.web.utils.json.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Configuration class for setting up the cookie serializer.
 */
@Slf4j
@Configuration
public class CookieConfiguration {

    /**
     * Bean method to configure the cookie serializer.
     *
     * @param cookieOptions The cookie options to be used for configuration.
     * @return The configured cookie serializer.
     */
    @Bean
    public CookieSerializer cookieSerializer(final CookieOptions cookieOptions) {
        log.debug("config default session cookie");

        final CookieOptions.SessionCookieOptions session = cookieOptions.getSession();

        final var serializer = new DefaultCookieSerializer();
        {
            serializer.setCookieName(session.getName());
            serializer.setCookiePath(session.getPath());
            serializer.setDomainNamePattern(session.getDomainPattern());
            serializer.setUseHttpOnlyCookie(session.isHttpOnly());
            serializer.setSameSite(session.getSameSite().getValue());
        }

        log.debug("session cookie configured {}{}", System.lineSeparator(), Json.lazyStringify(session));

        return serializer;
    }

}
