package by.sorface.passport.web.records.responses

import java.util.*

data class UserConfirm(val id: UUID, val email: String, val confirm: Boolean)
