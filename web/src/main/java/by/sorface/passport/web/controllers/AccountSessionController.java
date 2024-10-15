package by.sorface.passport.web.controllers;

import by.sorface.passport.web.facade.session.AccountSessionFacade;
import by.sorface.passport.web.records.sessions.CleanupSession;
import by.sorface.passport.web.records.sessions.UserContextSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class AccountSessionController {

    private final AccountSessionFacade accountSessionFacade;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{username}")
    public UserContextSession findByUsername(@PathVariable final String username) {
        return accountSessionFacade.findByUsername(username);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDelete(@RequestBody final CleanupSession cleanupSession) {
        accountSessionFacade.batchDelete(cleanupSession);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public UserContextSession getActiveSessions() {
        return accountSessionFacade.getCurrentActiveSessions();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public UserContextSession deleteCurrent(@RequestBody CleanupSession cleanupSession) {
        return accountSessionFacade.deleteSessions(cleanupSession);
    }

}
