package by.sorface.idp.config.security.cors

import by.sorface.idp.config.security.cors.properties.CorsConfigProperties
import by.sorface.idp.utils.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfiguration {

    private val logger: Logger = LoggerFactory.getLogger(CorsConfiguration::class.java)

    @Bean
    fun corsConfigurationSource(corsConfigProperties: CorsConfigProperties): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.allowedOrigins = corsConfigProperties.allowedOrigins
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")

        logger.info("CorsConfiguration source: {}", Json.stringify(configuration))

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }


}