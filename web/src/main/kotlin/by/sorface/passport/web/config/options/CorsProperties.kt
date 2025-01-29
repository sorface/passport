package by.sorface.passport.web.config.options

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("passport.cors")
open class CorsProperties {

    var options: List<CorsItemProperties> = arrayListOf()

    open class CorsItemProperties {
        lateinit var pattern: String
        var allowedOrigins: String? = null
        var allowedHeaders: String? = null
        var exposedHeaders: String? = null
        var allowedMethods: String? = null
        var allowCredentials: Boolean = false
        var maxAge: Long = 3600L
    }

}
