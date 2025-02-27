package by.devpav.kotlin.oidcidp.web.rest.model.accounts

data class AccountRegistration(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
)