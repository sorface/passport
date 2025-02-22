package by.devpav.kotlin.oidcidp.web.rest.controller

import by.sorface.passport.web.facade.session.AccountSessionFacade
import by.devpav.kotlin.oidcidp.web.rest.model.sessions.AccountCleanupSessionRequest
import by.devpav.kotlin.oidcidp.web.rest.model.sessions.AccountContextSession
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sessions")
class AccountSessionController(private val accountSessionFacade: AccountSessionFacade) {

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun findByUsername(@PathVariable @Valid username: @NotBlank String): AccountContextSession? = accountSessionFacade.findByUsername(username)

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getActiveSessions(): AccountContextSession? = accountSessionFacade.getActive()

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    fun deleteCurrent(@RequestBody accountCleanupSessionRequest: AccountCleanupSessionRequest?): AccountContextSession? = accountSessionFacade.deleteSessions(accountCleanupSessionRequest!!)

}
