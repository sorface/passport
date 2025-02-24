package by.devpav.kotlin.oidcidp

import by.devpav.kotlin.oidcidp.config.security.oauth2.properties.OidcAuthorizationProperties
import by.devpav.kotlin.oidcidp.config.security.csrf.properties.CsrfCookieProperties
import by.devpav.kotlin.oidcidp.config.web.properties.IdpEndpointProperties
import by.devpav.kotlin.oidcidp.config.web.properties.IdpFrontendEndpointProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(
    OidcAuthorizationProperties::class,
    CsrfCookieProperties::class,
    IdpEndpointProperties::class,
    IdpFrontendEndpointProperties::class
)
@SpringBootApplication
class OidcIdpApplication

fun main(args: Array<String>) {
    runApplication<OidcIdpApplication>(*args)
}
