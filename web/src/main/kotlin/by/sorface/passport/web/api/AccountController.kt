package by.sorface.passport.web.api

import by.sorface.passport.web.facade.accounts.AccountFacade
import by.sorface.passport.web.facade.signup.SignupEmailFacade
import by.sorface.passport.web.records.requests.AccountSignup
import by.sorface.passport.web.records.requests.UserPatchUpdate
import by.sorface.passport.web.records.responses.AccountSignupResponse
import by.sorface.passport.web.records.responses.ProfileRecord
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(private val accountFacade: AccountFacade, private val signupEmailFacade: SignupEmailFacade) {

    @GetMapping(value = ["/current"])
    fun getCurrentAuthorizedUser(): ProfileRecord = accountFacade.getCurrentAuthorizedUser()

    @PreAuthorize("isAnonymous()")
    @PostMapping(value = ["/signup"])
    fun signup(@RequestBody accountSignup: @Valid AccountSignup): AccountSignupResponse = signupEmailFacade.signup(accountSignup)
        .let { registeredUser ->
            AccountSignupResponse(registeredUser.id, registeredUser.username, accountSignup.email)
        }

    @PreAuthorize("@advancedSecurityEvaluator.hasUserId(#id) or hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    fun update(@RequestBody userPatchUpdate: UserPatchUpdate, @PathVariable id: UUID): ResponseEntity<*> {
        accountFacade.update(id, userPatchUpdate)
        return ResponseEntity.ok().build<Any>()
    }

}
