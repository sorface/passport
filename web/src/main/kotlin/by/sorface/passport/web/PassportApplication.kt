package by.sorface.passport.web

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableAspectJAutoProxy
@SpringBootApplication
open class PassportApplication

fun main(args: Array<String>) {
    SpringApplication.run(PassportApplication::class.java, *args)
}
