package by.sorface.passport.web.config.options.locale;

import by.sorface.passport.web.constants.SupportedLocales;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Data
@Component
@ConfigurationProperties("sorface.locale")
public class LocaleOptions {

    /**
     * The options for the locale cookie.
     */
    private Locale defaultLocale = SupportedLocales.RU;

    /**
     * The options for the locale cookie.
     */
    private String changeLocaleParameterName = "lang";

    /**
     * The options for the locale cookie.
     */
    private HttpMethod[] changeLocaleMethods = new HttpMethod[]{
            HttpMethod.POST
    };

    /**
     * The options for the locale cookie.
     */
    private LocaleCookieOptions cookie = new LocaleCookieOptions();

}
