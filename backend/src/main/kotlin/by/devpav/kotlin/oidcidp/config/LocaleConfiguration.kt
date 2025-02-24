package by.devpav.kotlin.oidcidp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import java.nio.charset.StandardCharsets

/**
 * Конфигурация локализации приложения.
 */
@Configuration
class LocaleConfiguration {

    companion object {
        /**
         * Расположение файла с сообщениями для локализации.
         */
        const val I18_BUNDLE_LOCATION: String = "language/messages"
    }

    /**
     * Создает и настраивает источник сообщений для локализации.
     *
     * @return источник сообщений для локализации.
     */
    @Bean
    fun resourceBundleMessageSource(): ResourceBundleMessageSource = ResourceBundleMessageSource().apply {
        setBasename(I18_BUNDLE_LOCATION)
        setDefaultEncoding(StandardCharsets.UTF_8.name())
    }

}