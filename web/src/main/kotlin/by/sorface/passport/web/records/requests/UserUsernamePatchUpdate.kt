package by.sorface.passport.web.records.requests

import jakarta.validation.constraints.NotNull

data class UserUsernamePatchUpdate(@NotNull val username: String? = null)
