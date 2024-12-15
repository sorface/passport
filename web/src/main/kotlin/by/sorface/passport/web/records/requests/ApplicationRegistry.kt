package by.sorface.passport.web.records.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class ApplicationRegistry(
    val name: @NotBlank(message = "validate.client.registry.name.empty-or-null") String?,
    val redirectionUrls: @NotNull(message = "validate.client.registry.redirect-url.null") @NotEmpty(message = "validate.client.registry.redirect-url.empty") MutableSet<String>?
)
