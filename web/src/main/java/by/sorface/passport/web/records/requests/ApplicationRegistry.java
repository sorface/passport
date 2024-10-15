package by.sorface.passport.web.records.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ApplicationRegistry(
        @NotBlank(message = "validate.client.registry.name.empty-or-null")
        String name,

        @NotNull(message = "validate.client.registry.redirect-url.null")
        @NotEmpty(message = "validate.client.registry.redirect-url.empty")
        Set<String> redirectionUrls
) {
}
