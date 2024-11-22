package by.sorface.passport.web.security.config

import by.sorface.passport.web.config.options.EndpointOptions
import by.sorface.passport.web.records.responses.OperationError
import by.sorface.passport.web.services.locale.LocaleI18Service
import by.sorface.passport.web.services.sleuth.SleuthService
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriUtils
import java.nio.charset.StandardCharsets
import java.util.function.BiFunction

@Component
class LoginFailureHandler(private val endpointOptions: EndpointOptions, private val sleuthService: SleuthService, private val localeI18Service: LocaleI18Service) :
    AuthenticationFailureHandler {

    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        val i18ErrorCode = if ((exception is BadCredentialsException)) "password.or.username.invalid" else "authentication.invalid"

        val operationError = buildError(HttpStatus.UNAUTHORIZED, i18ErrorCode)

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        response.outputStream.use { responseStream ->
            val mapper = ObjectMapper()
            mapper.writeValue(responseStream, operationError)
            responseStream.flush()
        }
    }

    private fun buildError(status: HttpStatus, message: String): OperationError {
        return OperationError(localeI18Service.getI18Message(message) ?: "", status.reasonPhrase, status.value(), sleuthService.getSpanId(), sleuthService.getTraceId())
    }

    companion object {
        private val BUILDER_URL = BiFunction { error: String?, url: String? -> url + "?error=" + UriUtils.encode(error!!, StandardCharsets.US_ASCII) }
    }
}
