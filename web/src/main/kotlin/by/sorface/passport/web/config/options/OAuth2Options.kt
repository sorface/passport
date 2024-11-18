package by.sorface.passport.web.config.options

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@ConfigurationProperties("sorface.oauth2")
open class OAuth2Options {

    var issuerUrl: String? = null

    val redis: RedisOptions = RedisOptions()

    class RedisOptions {

        var init: RedisDescriptionOptions? = null

        var complete: RedisDescriptionOptions? = null

        val consent: RedisDescriptionOptions = RedisDescriptionOptions()

    }

    class RedisDescriptionOptions {

        var prefix: String? = null

        var ttl: Long = 0

        var unit: TimeUnit = TimeUnit.SECONDS

    }
}


