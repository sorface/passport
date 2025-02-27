package by.devpav.kotlin.oidcidp.web.rest.model.apps

import by.devpav.kotlin.oidcidp.records.I18Codes
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.UUID

data class ApplicationDelete(
    @NotNull(message = I18Codes.I18ClientCodes.ID_MUST_BE_SET)
    @NotEmpty(message = I18Codes.I18ClientCodes.ID_CANNOT_BE_EMPTY)
    @UUID(message = I18Codes.I18ClientCodes.ID_IS_INVALID)
    val id: String?
)
