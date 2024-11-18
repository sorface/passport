package by.sorface.passport.web.config.options.locale

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("sorface.locale.cookie")
open class LocaleCookieOptions {
    lateinit var name: String

    var domain: String? = null

    var path: String? = null

    var httpOnly = false
}
