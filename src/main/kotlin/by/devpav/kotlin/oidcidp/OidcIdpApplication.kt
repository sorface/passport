package by.devpav.kotlin.oidcidp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties
@SpringBootApplication
class OidcIdpApplication

fun main(args: Array<String>) {
    runApplication<OidcIdpApplication>(*args)
}
