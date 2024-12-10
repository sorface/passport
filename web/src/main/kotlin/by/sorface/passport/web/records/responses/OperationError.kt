package by.sorface.passport.web.records.responses

import com.fasterxml.jackson.annotation.JsonIgnore

data class OperationError(
    val message: String,
    @JsonIgnore
    val details: String?,
    val code: Int,
    @JsonIgnore
    val spanId: String?,
    val traceId: String?
)
