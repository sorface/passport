package by.sorface.passport.web.config.security

import by.sorface.passport.web.config.options.CorsOptions
import by.sorface.passport.web.config.options.locale.LocaleOptions
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class WebConfig(private val corsOptions: CorsOptions, private val localeOptions: LocaleOptions) : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        corsOptions.options.forEach { corsItem ->
            registry.addMapping(corsItem.pattern)
                .allowedOrigins(corsItem.allowedOrigins)
                .allowedMethods(corsItem.allowedMethods)
                .allowedHeaders(corsItem.allowedHeaders)
                .exposedHeaders(corsItem.exposedHeaders)
                .allowCredentials(corsItem.allowCredentials)
                .maxAge(corsItem.maxAge)
        }
    }

}
