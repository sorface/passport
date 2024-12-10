package by.sorface.passport.web.records.responses

data class ValidateOperationError(val message: String, val errors: List<ValidateError>, val code: Int, val spanId: String?, val traceId: String?) {
    class ValidateError {
        var field: String? = null

        var message: String? = null
    }
}
