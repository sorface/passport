package by.sorface.idp.config.security.cors.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "idp.cors")
class CorsConfigProperties {

    var allowedOrigins = listOf("*")

}