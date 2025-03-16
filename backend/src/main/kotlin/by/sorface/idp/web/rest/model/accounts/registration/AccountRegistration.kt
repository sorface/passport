package by.sorface.idp.web.rest.model.accounts.registration

data class AccountRegistration(
    @Transient
    var registrationId: String? = null,
    val username: String,
    val password: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
)