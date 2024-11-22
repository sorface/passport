package by.sorface.passport.web.security.config

import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.responses.OperationError
import by.sorface.passport.web.services.locale.LocaleI18Service
import by.sorface.passport.web.services.sleuth.SleuthService
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class ExceptionAuthenticationEntryPoint(private val sleuthService: SleuthService, private val localeI18Service: LocaleI18Service) : AuthenticationEntryPoint {

    @Throws(IOException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        val operationError = buildError(HttpStatus.UNAUTHORIZED, I18Codes.I18GlobalCodes.ACCESS_DENIED)

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
}
