package by.sorface.idp.extencions

import by.sorface.idp.records.OperationError
import org.springframework.http.HttpStatus

fun OperationError.Companion.buildError(status: HttpStatus, message: String, spanId: String = "default", traceId: String = "default"): OperationError {
    return OperationError(message, status.reasonPhrase, status.value(), spanId, traceId)
}