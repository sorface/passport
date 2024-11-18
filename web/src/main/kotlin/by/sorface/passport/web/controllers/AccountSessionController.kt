package by.sorface.passport.web.controllers

import by.sorface.passport.web.facade.session.AccountSessionFacade
import by.sorface.passport.web.records.sessions.CleanupSession
import by.sorface.passport.web.records.sessions.UserContextSession
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
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
    fun batchDelete(@RequestBody cleanupSession: CleanupSession?): ResponseEntity<*> {
        accountSessionFacade.batchDelete(cleanupSession!!)

        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun activeSessions(): UserContextSession? = accountSessionFacade.getCurrentActiveSessions()

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    fun deleteCurrent(@RequestBody cleanupSession: CleanupSession?): UserContextSession? {
        return accountSessionFacade.deleteSessions(cleanupSession!!)
    }

}
