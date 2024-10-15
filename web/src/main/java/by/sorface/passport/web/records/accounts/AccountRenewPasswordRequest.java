package by.sorface.passport.web.records.accounts;

import by.sorface.passport.web.records.I18Codes;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AccountRenewPasswordRequest(
        @NotNull(message = I18Codes.I18UserCodes.EMAIL_NOT_VALID)
        @NotEmpty(message = I18Codes.I18UserCodes.EMAIL_NOT_VALID)
        // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?!.* ).{8,16}$", message = I18Codes.I18UserCodes.EMAIL_NOT_VALID)
        String email) {
}
