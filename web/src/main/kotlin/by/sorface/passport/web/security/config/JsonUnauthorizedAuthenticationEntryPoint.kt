package by.sorface.passport.web.security.config

import by.sorface.passport.web.records.I18Codes.I18AuthenticationCodes.NOT_AUTHENTICATED
import by.sorface.passport.web.records.responses.OperationError
import by.sorface.passport.web.security.extensions.useErrorJsonStream
import by.sorface.passport.web.services.locale.LocaleI18Service
import by.sorface.passport.web.services.sleuth.SleuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JsonUnauthorizedAuthenticationEntryPoint(private val sleuthService: SleuthService, private val localeI18Service: LocaleI18Service) : AuthenticationEntryPoint {

    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        val operationError = buildError(HttpStatus.UNAUTHORIZED, NOT_AUTHENTICATED)

        response.useErrorJsonStream(operationError)
    }

    private fun buildError(status: HttpStatus, message: String): OperationError {
        return OperationError(localeI18Service.getI18Message(message) ?: "", status.reasonPhrase, status.value(), sleuthService.getSpanId(), sleuthService.getTraceId())
    }
}
