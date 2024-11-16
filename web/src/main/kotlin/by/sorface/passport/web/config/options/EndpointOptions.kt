package by.sorface.passport.web.config.options

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "sorface.endpoint")
public open class EndpointOptions {
    /**
     * url signin page
     */
    var uriPageSignIn: String? = null

    /**
     * url signup page
     */
    var uriPageSignUp: String? = null

    /**
     * url profile page
     */
    var uriPageProfile: String? = null

    /**
     * url profile page
     */
    var uriPageFailure: String? = null

    /**
     * url not found page
     */
    var uriPageNotFound: String? = null

    /**
     * uri api signin
     */
    var uriApiLogin: String? = null

    /**
     * uri api logout
     */
    var uriApiLogout: String? = null
}
