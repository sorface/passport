package by.sorface.passport.web.config.options

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Data
@Configuration
@ConfigurationProperties("sorface.oauth2")
open class OAuth2Options {

    var issuerUrl: String? = null

    var redis: RedisOptions? = null

    class RedisOptions {

        var init: RedisDescriptionOptions? = null

        var complete: RedisDescriptionOptions? = null

        var consent: RedisDescriptionOptions? = null

    }

    /**
     * This class represents the Redis description options for OAuth2.
     */
    @Data
    class RedisDescriptionOptions {

        var prefix: String? = null

        var ttl: Long = 0

        var unit: TimeUnit? = null

    }
}


