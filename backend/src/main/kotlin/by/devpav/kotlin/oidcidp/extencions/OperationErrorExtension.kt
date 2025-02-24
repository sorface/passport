package by.devpav.kotlin.oidcidp.extencions

import by.devpav.kotlin.oidcidp.records.OperationError
import org.springframework.http.HttpStatus

fun OperationError.Companion.buildError(status: HttpStatus, message: String, spanId: String = "default", traceId: String = "default"): OperationError {
    return OperationError(message, status.reasonPhrase, status.value(), spanId, traceId)
}