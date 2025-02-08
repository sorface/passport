package by.sorface.passport.web.config.options

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "application.metadata")
data class ApplicationMetadata(val version: String)