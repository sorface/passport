package by.sorface.passport.web.security.extensions

import by.sorface.passport.web.records.responses.OperationError
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

fun HttpServletResponse.useErrorJsonStream(error: OperationError) {
    this.contentType = MediaType.APPLICATION_JSON_VALUE
    this.status = error.code
    this.outputStream.use { servletOutputStream -> ObjectMapper().writeValue(servletOutputStream, error) }
}

fun HttpServletResponse.useErrorJsonStream(status: HttpStatus, operationError: OperationError) {
    this.contentType = MediaType.APPLICATION_JSON_VALUE
    this.status = status.value()
    this.outputStream.use { servletOutputStream -> ObjectMapper().writeValue(servletOutputStream, operationError) }
}

fun <T> HttpServletResponse.useJsonStream(status: HttpStatus, obj: T) {
    this.contentType = MediaType.APPLICATION_JSON_VALUE
    this.status = status.value()
    this.outputStream.use { servletOutputStream -> ObjectMapper().writeValue(servletOutputStream, obj) }
}