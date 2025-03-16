package by.sorface.idp

import by.sorface.idp.config.security.csrf.properties.CsrfCookieProperties
import by.sorface.idp.config.security.oauth2.properties.OidcAuthorizationProperties
import by.sorface.idp.config.web.properties.IdpEndpointProperties
import by.sorface.idp.config.web.properties.IdpFrontendEndpointProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableJpaAuditing
@EnableConfigurationProperties(
    OidcAuthorizationProperties::class,
    CsrfCookieProperties::class,
    IdpEndpointProperties::class,
    IdpFrontendEndpointProperties::class
)
@SpringBootApplication
class IdpApplication

fun main(args: Array<String>) {
    runApplication<IdpApplication>(*args)
}
