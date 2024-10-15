package by.sorface.passport.web.config.options;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Data
@Configuration
@ConfigurationProperties("sorface.oauth2")
public class OAuth2Options {

    /**
     * The issuer URL for OAuth2.
     */
    private String issuerUrl;

    /**
     * The Redis options for OAuth2.
     */
    private RedisOptions redis;

    /**
     * This class represents the Redis options for OAuth2.
     */
    @Data
    public static class RedisOptions {

        /**
         * The Redis options for initialization.
         */
        private RedisDescriptionOptions init;

        /**
         * The Redis options for completion.
         */
        private RedisDescriptionOptions complete;

        /**
         * The Redis options for consent.
         */
        private RedisDescriptionOptions consent;

    }

    /**
     * This class represents the Redis description options for OAuth2.
     */
    @Data
    public static class RedisDescriptionOptions {

        /**
         * The prefix for the Redis key.
         */
        private String prefix;

        /**
         * The time to live value for the Redis key.
         */
        private long ttl;

        /**
         * The time unit for the time to live value.
         */
        private TimeUnit unit;

    }
}


