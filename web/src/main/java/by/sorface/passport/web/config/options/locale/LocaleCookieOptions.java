package by.sorface.passport.web.config.options.locale;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("sorface.locale.cookie")
public class LocaleCookieOptions {

    /**
     * The options for the locale cookie.
     */
    private String name;

    /**
     * The options for the locale cookie.
     */
    private String domain;

    /**
     * The options for the locale cookie.
     */
    private String path;

    /**
     * The options for the locale cookie.
     */
    private boolean httpOnly = false;

}
