package by.sorface.idp.web.rest.controller

import by.sorface.idp.web.rest.facade.ApplicationClientFacade
import by.sorface.idp.web.rest.model.apps.ApplicationClient
import by.sorface.idp.web.rest.model.apps.ApplicationPartialUpdate
import by.sorface.idp.web.rest.model.apps.ApplicationRefreshSecret
import by.sorface.idp.web.rest.model.apps.ApplicationRegistry
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import ru.sorface.boot.security.core.principal.SorfacePrincipal
import java.util.*

@RestController
@RequestMapping("/api/applications")
@PreAuthorize("hasRole('ADMIN')")
class ApplicationController(private val applicationClientFacade: ApplicationClientFacade) {

    @GetMapping
    fun findAllByUser(@AuthenticationPrincipal sorfacePrincipal: SorfacePrincipal): List<ApplicationClient> {
        return applicationClientFacade.findAllByUserId(sorfacePrincipal.id!!)
    }

    @GetMapping("/{id}")
    fun findByClientIdAndUser(
        @PathVariable("id") id: UUID,
        @AuthenticationPrincipal sorfacePrincipal: SorfacePrincipal
    ): ApplicationClient {
        return applicationClientFacade.findByIdAndUserId(id, sorfacePrincipal.id!!)
    }

    @PatchMapping("/{id}/refresh")
    fun refreshSecretByClientIdAndUser(
        @PathVariable("id") id: UUID,
        @AuthenticationPrincipal sorfacePrincipal: SorfacePrincipal
    ): ApplicationRefreshSecret {
        return applicationClientFacade.refreshSecretByIdAndUserId(id, sorfacePrincipal.id!!)
    }

    @PatchMapping("/{id}")
    fun partialUpdateByClientIdAndUser(
        @PathVariable("id") id: UUID,
        @RequestBody request: ApplicationPartialUpdate,
        @AuthenticationPrincipal sorfacePrincipal: SorfacePrincipal
    ): ApplicationClient {
        return applicationClientFacade.partialUpdateByIdAndUserId(id, sorfacePrincipal.id!!, request)
    }

    @DeleteMapping("/{id}")
    fun deleteByIdAndUser(
        @PathVariable id: @org.hibernate.validator.constraints.UUID UUID,
        @AuthenticationPrincipal sorfacePrincipal: SorfacePrincipal
    ) {
        applicationClientFacade.deleteByIdAndUserId(id, sorfacePrincipal.id!!)
    }

    @PostMapping
    fun registry(@RequestBody applicationRegistry: @Valid ApplicationRegistry): ApplicationClient =
        applicationClientFacade.registry(applicationRegistry)

}