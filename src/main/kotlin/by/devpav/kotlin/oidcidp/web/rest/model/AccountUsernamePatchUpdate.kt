package by.devpav.kotlin.oidcidp.web.rest.model

import jakarta.validation.constraints.NotNull

data class AccountUsernamePatchUpdate(@NotNull val username: String? = null)
