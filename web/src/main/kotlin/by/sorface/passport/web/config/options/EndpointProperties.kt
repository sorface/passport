package by.sorface.passport.web.config.options

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "passport.endpoint")
data class EndpointProperties(
    var uriPageSignIn: String? = null,
    var uriPageSignUp: String? = null,
    var uriPageProfile: String? = null,
    var uriPageFailure: String? = null,
    var uriPageNotFound: String? = null,
    var uriApiLogin: String? = null,
    var uriApiLogout: String? = null
)