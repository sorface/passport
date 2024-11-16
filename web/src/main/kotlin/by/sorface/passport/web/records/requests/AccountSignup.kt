package by.sorface.passport.web.records.requests

data class AccountSignup(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val email: String,
    val username: String,
    val password: String
)
