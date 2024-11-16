package by.sorface.passport.web.config.options

import by.sorface.passport.web.config.options.ClientTokenOptions.TokenSetting
import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.time.temporal.ChronoUnit

/**
 * This class represents the configuration options for the client token.
 * It is annotated with [ConfigurationProperties] to bind the properties in the application.properties file.
 * The class is also annotated with [Configuration] to indicate that it is a Spring configuration class.
 * It has a nested class [TokenSetting] to represent the settings for each type of token.
 *
 * @author Pavel Talaika
 * @version 1.0
 * @since 2024-10-03
 */
@Data
@Configuration
@ConfigurationProperties("sorface.client.token")
open class ClientTokenOptions {
    /**
     * The settings for the access token.
     */
    var accessToken = TokenSetting()

    /**
     * The settings for the access token.
     */
    var refreshToken = TokenSetting()

    /**
     * The settings for the access token.
     */
    var authorizationCode = TokenSetting()

    /**
     * The settings for the token.
     */
    @Data
    class TokenSetting {
        /**
         * The time to live value for the token.
         */
        var timeToLiveValue: Long = 0

        /**
         * The time to live value for the token.
         */
        var timeToLiveCron: ChronoUnit? = null
    }
}
