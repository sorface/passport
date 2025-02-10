package by.devpav.kotlin.oidcidp.extencions

import by.devpav.kotlin.oidcidp.records.OperationError
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