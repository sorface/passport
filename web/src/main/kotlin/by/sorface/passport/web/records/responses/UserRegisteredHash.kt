package by.sorface.passport.web.records.responses

import java.util.*

data class UserRegisteredHash(val id: UUID, val email: String, val hash: String, val firstName: String, val lastName: String)
