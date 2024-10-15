package by.sorface.passport.web.controllers;

import by.sorface.passport.web.facade.clients.ApplicationClientFacade;
import by.sorface.passport.web.records.I18Codes;
import by.sorface.passport.web.records.requests.ApplicationClientPatchRequest;
import by.sorface.passport.web.records.requests.ApplicationRegistry;
import by.sorface.passport.web.records.responses.ApplicationClient;
import by.sorface.passport.web.records.responses.ApplicationClientRefreshSecret;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class ApplicationClientController {

    private final ApplicationClientFacade applicationClientFacade;

    @GetMapping
    public List<ApplicationClient> findAllForCurrent() {
        return applicationClientFacade.findByCurrentUser();
    }

    @GetMapping("/{clientId}")
    public ApplicationClient getById(@PathVariable("clientId") final UUID clientId) {
        return applicationClientFacade.getByIdAndCurrentUser(clientId);
    }

    @PatchMapping("/{clientId}/refresh")
    public ApplicationClientRefreshSecret refreshSecret(@PathVariable("clientId") final UUID clientId) {
        return applicationClientFacade.refreshSecret(clientId);
    }

    @PatchMapping("/{clientId}")
    public ApplicationClient partialUpdate(@PathVariable("clientId") final UUID clientId,
                                           @RequestBody final ApplicationClientPatchRequest request) {
        return applicationClientFacade.partialUpdate(clientId, request);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> delete(@NotNull(message = I18Codes.I18ClientCodes.ID_MUST_BE_SET)
                                    @NotEmpty(message = I18Codes.I18ClientCodes.ID_CANNOT_BE_EMPTY)
                                    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = I18Codes.I18ClientCodes.ID_IS_INVALID)
                                    @PathVariable String clientId) {
        applicationClientFacade.delete(UUID.fromString(clientId));

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ApplicationClient registry(@RequestBody @Valid final ApplicationRegistry applicationRegistry) {
        return applicationClientFacade.registry(applicationRegistry);
    }

}
