package by.sorface.passport.web.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "passport.account.registry")
class AccountRegistryProperties {

    var liveToCacheSeconds: Long = 300

}