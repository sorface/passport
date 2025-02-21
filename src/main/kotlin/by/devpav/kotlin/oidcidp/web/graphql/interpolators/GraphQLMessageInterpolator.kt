package by.devpav.kotlin.oidcidp.web.graphql.interpolators

import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.records.SupportedLocales
import graphql.GraphQLError
import graphql.validation.interpolation.MessageInterpolator
import graphql.validation.rules.ValidationEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component
import java.util.*

@Component
class GraphQLMessageInterpolator : MessageInterpolator {

    @Autowired
    private lateinit var resourceBundleMessageSource: ResourceBundleMessageSource

    override fun interpolate(messageTemplate: String?, messageParams: MutableMap<String, Any>, validationEnvironment: ValidationEnvironment): GraphQLError {
        return GraphQLError.newError()
            .message(getLocalMessageOrNull(messageTemplate, validationEnvironment.locale))
            .extensions(
                mapOf(
                    "locale" to validationEnvironment.locale
                )
            )
            .location(validationEnvironment.location)
            .path(validationEnvironment.validatedPath)
            .errorType(ErrorType.BAD_REQUEST)
            .build()
    }

    private fun getLocalMessageOrNull(i18Code: String?, locale: Locale) = kotlin.runCatching {
        i18Code ?: return@runCatching resourceBundleMessageSource.getMessage(
            I18Codes.I18GlobalCodes.VALIDATION_ERROR, null, SupportedLocales.getSupportedLocaleOrDefault(locale.language, Locale.ENGLISH)
        )

        resourceBundleMessageSource.getMessage(i18Code, null, SupportedLocales.getSupportedLocaleOrDefault(locale.language, Locale.ENGLISH))
    }.getOrElse { i18Code }

}