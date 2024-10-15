package by.sorface.passport.web.config.options;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("sorface.cors")
public class CorsOptions {

    /**
     * The list of CORS options.
     */
    private List<CorsItemOptions> options;

    /**
     * This class represents the CORS item options.
     */
    @Data
    public static class CorsItemOptions {

        /**
         * The pattern for the CORS item.
         */
        private String pattern;

        /**
         * The allowed origins for the CORS item.
         */
        private String allowedOrigins;

        /**
         * The allowed origin patterns for the CORS item.
         */
        private String allowedOriginPatterns;

        /**
         * The allowed headers for the CORS item.
         */
        private String allowedHeaders;

        /**
         * The exposed headers for the CORS item.
         */
        private String exposedHeaders;

        /**
         * The allowed methods for the CORS item.
         */
        private String allowedMethods;

        /**
         * Whether credentials are allowed for the CORS item.
         */
        private boolean allowCredentials;

        /**
         * The maximum age for the CORS item.
         */
        private Long maxAge = 3600L;

    }

}
