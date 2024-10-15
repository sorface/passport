package by.sorface.passport.web.config.options;

import lombok.Data;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("sorface.cookie")
public class CookieOptions {

    /**
     * The session cookie options.
     */
    private SessionCookieOptions session;

    /**
     * The session cookie options.
     */
    private CsrfCookieOptions csrf;

    /**
     * The session cookie options.
     */
    @Data
    public static class SessionCookieOptions {

        /**
         * The session cookie options.
         */
        private String domainPattern;

        /**
         * The path for the session cookie.
         */
        private String path;

        /**
         * The path for the session cookie.
         */
        private String name;

        /**
         * The path for the session cookie.
         */
        private SameSiteCookies sameSite;

        /**
         * Whether the session cookie is only accessible via HTTP.
         */
        private boolean httpOnly = false;

    }

    /**
     * Whether the session cookie is only accessible via HTTP.
     */
    @Data
    public static class CsrfCookieOptions {

        /**
         * The domain for the CSRF cookie.
         */
        private String domain;

        /**
         * The domain for the CSRF cookie.
         */
        private String path;

        /**
         * The domain for the CSRF cookie.
         */
        private String name;

        /**
         * Whether the CSRF cookie is only accessible via HTTP.
         */
        private boolean httpOnly = false;

    }
}
