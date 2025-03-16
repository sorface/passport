package by.sorface.idp.web.rest.controller

import by.sorface.idp.web.rest.facade.AccountSessionFacade
import by.sorface.idp.web.rest.model.sessions.AccountCleanupSessionRequest
import by.sorface.idp.web.rest.model.sessions.AccountContextSession
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sessions")
class AccountSessionController(private val accountSessionFacade: AccountSessionFacade) {

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun getAllByUsername(@PathVariable @Valid username: @NotBlank String): AccountContextSession =
        accountSessionFacade.getAllByUsername(username)

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun deleteMultipleByUsername(@PathVariable username: String, @RequestBody accountCleanupSessionRequest: AccountCleanupSessionRequest): AccountContextSession =
        accountSessionFacade.deleteMultipleByUsername(username, accountCleanupSessionRequest)

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getAll(): AccountContextSession =
        accountSessionFacade.getAll()

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    fun deleteMultiple(@RequestBody accountCleanupSessionRequest: AccountCleanupSessionRequest): AccountContextSession =
        accountSessionFacade.deleteMultiple(accountCleanupSessionRequest)

}
