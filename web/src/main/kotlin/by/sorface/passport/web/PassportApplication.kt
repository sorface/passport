package by.sorface.passport.web

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties

@EnableConfigurationProperties
@ConfigurationPropertiesScan
@SpringBootApplication
class PassportApplication

fun main(args: Array<String>) {
    SpringApplication.run(PassportApplication::class.java, *args)
}
