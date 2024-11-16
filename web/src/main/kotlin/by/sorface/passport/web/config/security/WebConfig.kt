package by.sorface.passport.web.config.security

import by.sorface.passport.web.config.options.CorsOptions
import by.sorface.passport.web.config.options.locale.LocaleCookieOptions
import by.sorface.passport.web.config.options.locale.LocaleOptions
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import java.util.*

@Configuration
@RequiredArgsConstructor
open class WebConfig(
    private val corsOptions: CorsOptions,
    private val localeOptions: LocaleOptions
) : WebMvcConfigurer {

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

    @Bean
    open fun localeResolver(): LocaleResolver {
        val cookieOptions: LocaleCookieOptions = localeOptions.cookie

        val cookieLocaleResolver = CookieLocaleResolver(cookieOptions.name)

        cookieLocaleResolver.setCookieDomain(cookieOptions.domain)
        cookieLocaleResolver.setCookiePath(cookieOptions.path)
        cookieLocaleResolver.setDefaultLocale(localeOptions.defaultLocale)
        cookieLocaleResolver.setCookieHttpOnly(false)
        cookieLocaleResolver.setCookieSecure(true)

        return cookieLocaleResolver
    }

    @Bean
    open fun localeChangeInterceptor(): LocaleChangeInterceptor {
        val localeChangeInterceptor = LocaleChangeInterceptor()

        val httpMethods = localeOptions.changeLocaleMethods.map { obj: HttpMethod -> obj.name() }.toTypedArray()

        localeChangeInterceptor.paramName = localeOptions.changeLocaleParameterName
        localeChangeInterceptor.setHttpMethods(*httpMethods)

        return localeChangeInterceptor
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localeChangeInterceptor())
    }
}
