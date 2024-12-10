package by.sorface.passport.web.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "passport.account.registry.cookie")
class AccountCookieProperties {

    var domain: String? = null

    var path: String? = null

    var name: String? = null

    var httpOnly = true

    var secure = true

}