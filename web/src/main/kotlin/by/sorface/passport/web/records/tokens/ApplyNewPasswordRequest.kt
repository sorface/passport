package by.sorface.passport.web.records.tokens

@JvmRecord
data class ApplyNewPasswordRequest(
    val newPassword: String,
    val hashToken: String
)
