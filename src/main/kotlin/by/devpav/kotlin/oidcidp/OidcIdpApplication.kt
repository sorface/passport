package by.devpav.kotlin.oidcidp

import by.devpav.kotlin.oidcidp.config.OidcAuthorizationProperties
import by.devpav.kotlin.oidcidp.config.csrf.properties.CsrfCookieProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(
    OidcAuthorizationProperties::class,
    CsrfCookieProperties::class
)
@SpringBootApplication
class OidcIdpApplication

fun main(args: Array<String>) {
    runApplication<OidcIdpApplication>(*args)
}
