package by.sorface.idp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableJpaAuditing
@ConfigurationPropertiesScan(basePackages = [
    "by.sorface.idp.config.security.oauth2.properties",
    "by.sorface.idp.config.web.properties"
])
@EnableConfigurationProperties
@SpringBootApplication
class IdpApplication

fun main(args: Array<String>) {
    runApplication<IdpApplication>(*args)
}
