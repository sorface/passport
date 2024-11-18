package by.sorface.passport.web.records.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class AccountSignup(
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val email: @NotBlank @Email String,
    val username: @NotBlank String,
    val password: @NotBlank String
)
