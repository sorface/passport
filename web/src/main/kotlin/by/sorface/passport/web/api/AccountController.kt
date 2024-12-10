package by.sorface.passport.web.api

import by.sorface.passport.web.facade.accounts.AccountFacade
import by.sorface.passport.web.records.requests.UserPatchUpdate
import by.sorface.passport.web.records.responses.ProfileRecord
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(private val accountFacade: AccountFacade) {

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = ["/current"])
    fun getCurrentAuthorizedUser(): ProfileRecord = accountFacade.getCurrentAuthorizedUser()

    @PatchMapping("/{id}")
    @PreAuthorize("@advancedSecurityEvaluator.hasPrincipalId(#id) or hasAuthority('ADMIN')")
    fun update(@RequestBody userPatchUpdate: UserPatchUpdate, @PathVariable id: UUID): ProfileRecord = accountFacade.update(id, userPatchUpdate)

}
