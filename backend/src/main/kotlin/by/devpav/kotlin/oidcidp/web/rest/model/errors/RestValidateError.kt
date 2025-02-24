package by.devpav.kotlin.oidcidp.web.rest.model.errors

import org.springframework.http.HttpStatus

data class RestValidateError(val message: String, val errors: List<ValidateError>, val code: Int = HttpStatus.BAD_REQUEST.value(), val spanId: String? = null, val traceId: String? = null) {

    class ValidateError {
        var field: String? = null

        var message: String? = null
    }

}
