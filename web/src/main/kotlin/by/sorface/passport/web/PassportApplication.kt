package by.sorface.passport.web

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class PassportApplication

fun main(args: Array<String>) {
    SpringApplication.run(PassportApplication::class.java, *args)
}
