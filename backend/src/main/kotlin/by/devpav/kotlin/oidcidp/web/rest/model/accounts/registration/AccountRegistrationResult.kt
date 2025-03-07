package by.devpav.kotlin.oidcidp.web.rest.model.accounts.registration

import java.time.Instant

data class AccountRegistrationResult(
    var registrationId: String,
    var otpExpiredTime: Instant
)
