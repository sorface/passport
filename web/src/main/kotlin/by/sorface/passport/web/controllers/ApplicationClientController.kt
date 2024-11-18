package by.sorface.passport.web.controllers

import by.sorface.passport.web.facade.clients.ApplicationClientFacade
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.requests.ApplicationClientPatchRequest
import by.sorface.passport.web.records.requests.ApplicationRegistry
import by.sorface.passport.web.records.responses.ApplicationClient
import by.sorface.passport.web.records.responses.ApplicationClientRefreshSecret
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/applications")
@PreAuthorize("hasAuthority('ADMIN')")
open class ApplicationClientController(private val applicationClientFacade: ApplicationClientFacade) {

    @GetMapping
    fun findAllForCurrent(): List<ApplicationClient> {
        return applicationClientFacade.findByCurrentUser()
    }

    @GetMapping("/{clientId}")
    fun getById(@PathVariable("clientId") clientId: UUID?): ApplicationClient? {
        return applicationClientFacade.getByIdAndCurrentUser(clientId!!)
    }

    @PatchMapping("/{clientId}/refresh")
    fun refreshSecret(@PathVariable("clientId") clientId: UUID?): ApplicationClientRefreshSecret? {
        return applicationClientFacade.refreshSecret(clientId!!)
    }

    @PatchMapping("/{clientId}")
    fun partialUpdate(@PathVariable("clientId") clientId: UUID, @RequestBody request: ApplicationClientPatchRequest): ApplicationClient? {
        return applicationClientFacade.partialUpdate(clientId, request)
    }

    @DeleteMapping("/{clientId}")
    fun delete(
        @PathVariable
        clientId:
        @NotNull(message = I18Codes.I18ClientCodes.ID_MUST_BE_SET)
        @NotEmpty(message = I18Codes.I18ClientCodes.ID_CANNOT_BE_EMPTY)
        @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = I18Codes.I18ClientCodes.ID_IS_INVALID) String?
    ): ResponseEntity<*> {
        applicationClientFacade.delete(UUID.fromString(clientId))

        return ResponseEntity.ok().build<Any>()
    }

    @PostMapping
    fun registry(@RequestBody applicationRegistry: @Valid ApplicationRegistry): ApplicationClient? {
        return applicationClientFacade.registry(applicationRegistry)
    }
}
