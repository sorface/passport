package by.sorface.passport.web.config.options

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Data
@Configuration
@ConfigurationProperties("sorface.cors")
open class CorsOptions {

    /**
     * The list of CORS options.
     */
    lateinit var options: List<CorsItemOptions>

    /**
     * This class represents the CORS item options.
     */
    @Data
    class CorsItemOptions {
        /**
         * The pattern for the CORS item.
         */
        lateinit var pattern: String

        /**
         * The allowed origins for the CORS item.
         */
        var allowedOrigins: String? = null

        /**
         * The allowed origin patterns for the CORS item.
         */
        var allowedOriginPatterns: String? = null

        /**
         * The allowed headers for the CORS item.
         */
        var allowedHeaders: String? = null

        /**
         * The exposed headers for the CORS item.
         */
        var exposedHeaders: String? = null

        /**
         * The allowed methods for the CORS item.
         */
        var allowedMethods: String? = null

        /**
         * Whether credentials are allowed for the CORS item.
         */
        var allowCredentials = false

        /**
         * The maximum age for the CORS item.
         */
        var maxAge = 3600L
    }
}
