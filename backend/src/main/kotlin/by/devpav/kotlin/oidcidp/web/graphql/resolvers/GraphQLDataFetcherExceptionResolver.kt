package by.devpav.kotlin.oidcidp.web.graphql.resolvers

import by.devpav.kotlin.oidcidp.exceptions.GraphqlException
import by.devpav.kotlin.oidcidp.exceptions.GraphqlServerException
import by.devpav.kotlin.oidcidp.exceptions.GraphqlUserException
import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.records.SupportedLocales.Companion.getSupportedLocaleOrDefault
import graphql.GraphQLError
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.stereotype.Component
import java.util.*

@Component
class GraphQLDataFetcherExceptionResolver : DataFetcherExceptionResolverAdapter() {

    @Autowired
    private lateinit var resourceBundleMessageSource: ResourceBundleMessageSource

    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        return GraphQLError.newError()
            .message(resolveErrorMessage(ex, env))
            .extensions(
                mapOf(
                    "locale" to env.locale
                )
            )
            .location(env.field.sourceLocation)
            .path(env.executionStepInfo.path)
            .errorType(resolveClassificationError(ex, env))
            .build()
    }

    fun resolveClassificationError(ex: Throwable, env: DataFetchingEnvironment): ErrorType {
        return when (ex) {
            is AuthorizationDeniedException -> ErrorType.FORBIDDEN
            is AccessDeniedException -> ErrorType.UNAUTHORIZED
            is GraphqlUserException -> ErrorType.BAD_REQUEST
            is GraphqlServerException -> ErrorType.INTERNAL_ERROR
            is GraphqlException -> ErrorType.INTERNAL_ERROR

            else -> ErrorType.INTERNAL_ERROR
        }
    }

    fun resolveErrorMessage(ex: Throwable, env: DataFetchingEnvironment): String? {
        return when (ex) {
            is AuthorizationDeniedException -> {
                getLocalMessageOrNull(I18Codes.I18AuthenticationCodes.NOT_AUTHENTICATED, env.locale)
            }

            is AccessDeniedException -> {
                getLocalMessageOrNull(I18Codes.I18GlobalCodes.ACCESS_DENIED, env.locale)
            }

            is GraphqlException -> {
                getLocalMessageOrNull(ex.i18Code, env.locale)
            }

            else -> getLocalMessageOrNull(I18Codes.I18AuthenticationCodes.NOT_AUTHENTICATED, env.locale)
        }
    }

    private fun getLocalMessageOrNull(i18Code: String, locale: Locale) = kotlin.runCatching {
        resourceBundleMessageSource.getMessage(i18Code, null, getSupportedLocaleOrDefault(locale.language, Locale.ENGLISH))
    }.getOrElse { i18Code }

}
