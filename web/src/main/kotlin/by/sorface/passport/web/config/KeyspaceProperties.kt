package by.sorface.passport.web.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

open class KeyspaceProperties {
    var lifeTime: Duration = Duration.ofMinutes(5)
    var keyspace: String = ""
}

@ConfigurationProperties(prefix = "passport.account.registry")
open class AccountRegistryKeyspace : KeyspaceProperties()

@ConfigurationProperties(prefix = "passport.account.registry.one-time-password")
open class AccountRegistryOneTimePasswordKeyspace : KeyspaceProperties()