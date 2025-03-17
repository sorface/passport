package by.sorface.idp.config.web.autostart

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.env.AbstractEnvironment
import org.springframework.core.env.EnumerablePropertySource
import org.springframework.core.env.Environment
import org.springframework.core.env.PropertySource
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.StreamSupport
import kotlin.collections.Set


@Component
class PropertyLogger {

    private val secretNameEnvironments: Set<String> = setOf(
        "secret",
        "credentials",
        "password",
        "username"
    )

    @EventListener
    fun handleContextRefresh(event: ContextRefreshedEvent) {
        val env: Environment = event.applicationContext.environment

        LOGGER.info("====== Environment and configuration ======")

        LOGGER.info("Active profiles: {}", env.activeProfiles.contentToString())

        val sources = (env as AbstractEnvironment).propertySources

        StreamSupport.stream(sources.spliterator(), false)
            .filter { ps: PropertySource<*>? -> ps is EnumerablePropertySource<*> }
            .map<Array<String>> { ps: PropertySource<*> -> (ps as EnumerablePropertySource).propertyNames }
            .flatMap(Arrays::stream)
            .distinct()
            .filter { prop: String -> secretNameEnvironments.none { prop.contains(it) } }
            .forEach { prop: String -> LOGGER.info("$prop: ${env.getProperty(prop)}") }

        LOGGER.info("===========================================")
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(PropertyLogger::class.java)
    }
}