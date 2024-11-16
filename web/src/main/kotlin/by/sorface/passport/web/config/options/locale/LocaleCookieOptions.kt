package by.sorface.passport.web.config.options.locale

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Data
@Component
@ConfigurationProperties("sorface.locale.cookie")
open class LocaleCookieOptions {
    /**
     * The options for the locale cookie.
     */
    var name: String? = null

    /**
     * The options for the locale cookie.
     */
    var domain: String? = null

    /**
     * The options for the locale cookie.
     */
    var path: String? = null

    /**
     * The options for the locale cookie.
     */
    var httpOnly = false
}
