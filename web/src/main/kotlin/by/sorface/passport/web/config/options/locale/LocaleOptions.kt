package by.sorface.passport.web.config.options.locale

import by.sorface.passport.web.constants.SupportedLocales
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.util.*

@Component
@ConfigurationProperties("sorface.locale")
open class LocaleOptions {

    /**
     * The options for the locale cookie.
     */
    var defaultLocale: Locale = SupportedLocales.RU.locale

    /**
     * The options for the locale cookie.
     */
    var changeLocaleParameterName = "lang"

    /**
     * The options for the locale cookie.
     */
    var changeLocaleMethods = arrayOf(
        HttpMethod.POST
    )

    /**
     * The options for the locale cookie.
     */
    var cookie = LocaleCookieOptions()

}
