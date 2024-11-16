package by.sorface.passport.web.records.accounts

import by.sorface.passport.web.records.I18Codes
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class AccountRenewPasswordRequest(
    @NotNull(message = I18Codes.I18UserCodes.EMAIL_NOT_VALID) @NotEmpty(message = I18Codes.I18UserCodes.EMAIL_NOT_VALID) val email: String?
)
