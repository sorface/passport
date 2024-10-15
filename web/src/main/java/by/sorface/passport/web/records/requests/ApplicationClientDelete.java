package by.sorface.passport.web.records.requests;

import by.sorface.passport.web.records.I18Codes;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ApplicationClientDelete(
        @NotNull(message = I18Codes.I18ClientCodes.ID_MUST_BE_SET)
        @NotEmpty(message = I18Codes.I18ClientCodes.ID_CANNOT_BE_EMPTY)
        @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = I18Codes.I18ClientCodes.ID_IS_INVALID)
        String id) {
}
