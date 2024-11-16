package by.sorface.passport.web.records.responses

data class OperationError(
    val message: String,
    val details: String?,
    val code: Int,
    val spanId: String?,
    val traceId: String?
)
