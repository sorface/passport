package by.sorface.passport.web.config.options;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sorface.endpoint")
public class EndpointOptions {

    /**
     * url signin page
     */
    private String uriPageSignIn;

    /**
     * url signup page
     */
    private String uriPageSignUp;

    /**
     * url profile page
     */
    private String uriPageProfile;

    /**
     * url profile page
     */
    private String uriPageFailure;

    /**
     * url not found page
     */
    private String uriPageNotFound;

    /**
     * uri api signin
     */
    private String uriApiLogin;

    /**
     * uri api logout
     */
    private String uriApiLogout;

}
