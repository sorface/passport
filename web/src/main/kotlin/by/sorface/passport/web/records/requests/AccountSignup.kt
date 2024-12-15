package by.sorface.passport.web.records.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class AccountSignup(
    val email: @NotBlank @Email String,
    val username: @NotBlank String,
    val password: @NotBlank String
)
