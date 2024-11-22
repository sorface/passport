package by.sorface.passport.web.security.config

import by.sorface.passport.web.config.options.CorsOptions
import by.sorface.passport.web.constants.SupportedLocales
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver


@Configuration
class WebConfig(private val corsOptions: CorsOptions) : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) =
        corsOptions.options.forEach { corsItem ->
            registry.addMapping(corsItem.pattern)
                .allowedOrigins(corsItem.allowedOrigins)
                .allowedMethods(corsItem.allowedMethods)
                .allowedHeaders(corsItem.allowedHeaders)
                .exposedHeaders(corsItem.exposedHeaders)
                .allowCredentials(corsItem.allowCredentials)
                .maxAge(corsItem.maxAge)
        }

    @Bean
    fun localeResolver(): LocaleResolver {
        val localeResolver = AcceptHeaderLocaleResolver()

        localeResolver.setDefaultLocale(SupportedLocales.RU.locale)
        localeResolver.supportedLocales = SupportedLocales.entries.map { it.locale }

        return localeResolver
    }

}
