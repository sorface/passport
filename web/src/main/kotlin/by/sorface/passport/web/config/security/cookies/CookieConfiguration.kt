package by.sorface.passport.web.config.security.cookies

import by.sorface.passport.web.config.options.CookieOptions
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.web.http.CookieSerializer
import org.springframework.session.web.http.DefaultCookieSerializer

/**
 * Configuration class for setting up the cookie serializer.
 */
@Slf4j
@Configuration
open class CookieConfiguration {
    /**
     * Bean method to configure the cookie serializer.
     *
     * @param cookieOptions The cookie options to be used for configuration.
     * @return The configured cookie serializer.
     */
    @Bean
    open fun cookieSerializer(cookieOptions: CookieOptions): CookieSerializer {
        val sessionCookieOptions = cookieOptions.session ?: throw IllegalArgumentException("session properties can't be null")

        return sessionCookieOptions.let { sessionProperties ->
            val serializer = DefaultCookieSerializer()

            serializer.setCookieName(sessionProperties.name)
            serializer.setCookiePath(sessionProperties.path)
            serializer.setDomainNamePattern(sessionProperties.domainPattern)
            serializer.setUseHttpOnlyCookie(sessionProperties.httpOnly)

            sessionProperties.sameSite?.let { siteProperties ->
                serializer.setSameSite(siteProperties.value)
            }

            serializer
        }
    }
}
