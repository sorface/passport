package by.devpav.kotlin.oidcidp.config.entrypoints

import by.devpav.kotlin.oidcidp.extencions.useErrorJsonStream
import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.records.I18Codes.I18AuthenticationCodes.NOT_AUTHENTICATED
import by.devpav.kotlin.oidcidp.service.I18Service
import by.devpav.kotlin.oidcidp.web.rest.model.errors.RestError
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JsonUnauthorizedAuthenticationEntryPoint(private val i18Service: I18Service) : AuthenticationEntryPoint {

    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        val operationError = buildError(HttpStatus.UNAUTHORIZED, NOT_AUTHENTICATED)

        response.useErrorJsonStream(operationError)
    }

    private fun buildError(status: HttpStatus, message: String): RestError {
        val i18Message = i18Service.getI18MessageOrDefault(message, defaultCode = I18Codes.I18GlobalCodes.ACCESS_DENIED)

        return RestError(i18Message,status.value())
    }
}
