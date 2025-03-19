package by.sorface.idp.config.web.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Класс, содержащий метаданные приложения
 *
 * @property version версия приложения
 */
@ConfigurationProperties(prefix = "spring.application")
data class MetaApplicationProperties(var version: String? = null)