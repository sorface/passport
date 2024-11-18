package by.sorface.passport.web.records.tokens

data class ApplyNewPasswordRequest(
    val newPassword: String,
    val hashToken: String
)
