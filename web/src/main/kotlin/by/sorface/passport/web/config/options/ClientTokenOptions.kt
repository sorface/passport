package by.sorface.passport.web.config.options

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.time.temporal.ChronoUnit

@Configuration
@ConfigurationProperties("sorface.client.token")
open class ClientTokenOptions {

    val accessToken = TokenSetting()

    val refreshToken = TokenSetting()

    val authorizationCode = TokenSetting()

    class TokenSetting {
        var timeToLiveValue: Long = 0

        var timeToLiveCron: ChronoUnit? = null
    }
}
