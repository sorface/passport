package by.sorface.passport.web.config.options;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.temporal.ChronoUnit;

/**
 * This class represents the configuration options for the client token.
 * It is annotated with {@link ConfigurationProperties} to bind the properties in the application.properties file.
 * The class is also annotated with {@link Configuration} to indicate that it is a Spring configuration class.
 * It has a nested class {@link TokenSetting} to represent the settings for each type of token.
 *
 * @author Pavel Talaika
 * @version 1.0
 * @since 2024-10-03
 */
@Data
@Configuration
@ConfigurationProperties("sorface.client.token")
public class ClientTokenOptions {

    /**
     * The settings for the access token.
     */
    private TokenSetting accessToken = new TokenSetting();

    /**
     * The settings for the access token.
     */
    private TokenSetting refreshToken = new TokenSetting();

    /**
     * The settings for the access token.
     */
    private TokenSetting authorizationCode = new TokenSetting();

    /**
     * The settings for the token.
     */
    @Data
    public static class TokenSetting {

        /**
         * The time to live value for the token.
         */
        private int timeToLiveValue;

        /**
         * The time to live value for the token.
         */
        private ChronoUnit timeToLiveCron;

    }

}
