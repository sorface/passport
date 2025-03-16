package by.sorface.idp.web.rest.model.apps

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class ApplicationRegistry(

    @NotBlank(message = "validate.client.registry.name.empty-or-null")
    val name: String?,

    @Size(min = 1)
    @NotNull
    @NotEmpty
    val redirectionUrls: Set<String> = setOf()

)
