package by.sorface.passport.web.api

import by.sorface.passport.web.facade.session.AccountSessionFacade
import by.sorface.passport.web.records.sessions.CleanupSession
import by.sorface.passport.web.records.sessions.UserContextSession
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sessions")
open class AccountSessionController(private val accountSessionFacade: AccountSessionFacade) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{username}")
    fun findByUsername(@PathVariable @Valid username: @NotBlank String): UserContextSession? {
        return accountSessionFacade.findByUsername(username)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/batch")
    fun batchDelete(@RequestBody cleanupSession: CleanupSession): Set<String> {
        return accountSessionFacade.batchDelete(cleanupSession)
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun activeSessions(): UserContextSession? = accountSessionFacade.getActiveSessions()

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    fun deleteCurrent(@RequestBody cleanupSession: CleanupSession?): UserContextSession? = accountSessionFacade.deleteSessions(cleanupSession!!)

}
