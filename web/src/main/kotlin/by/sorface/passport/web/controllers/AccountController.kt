package by.sorface.passport.web.controllers

import by.sorface.passport.web.facade.accounts.AccountFacade
import by.sorface.passport.web.facade.signup.SignupEmailFacade
import by.sorface.passport.web.records.requests.AccountSignup
import by.sorface.passport.web.records.requests.ResendConfirmEmail
import by.sorface.passport.web.records.requests.UserPatchUpdate
import by.sorface.passport.web.records.responses.AccountSignupResponse
import by.sorface.passport.web.records.responses.ProfileRecord
import by.sorface.passport.web.records.responses.UserRegistered
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/accounts")
class AccountController @Autowired constructor(private val accountFacade: AccountFacade, private val signupEmailFacade: SignupEmailFacade) {

    @GetMapping(value = ["/current"])
    fun getCurrentAuthorizedUser(): ProfileRecord = accountFacade.getCurrentAuthorizedUser()

    @PreAuthorize("isAnonymous()")
    @PostMapping(value = ["/signup"])
    fun signup(@RequestBody accountSignup: AccountSignup): AccountSignupResponse = signupEmailFacade.signup(accountSignup)
        .let { registeredUser ->
            AccountSignupResponse(registeredUser.id, registeredUser.username, accountSignup.email)
        }

    @PreAuthorize("@sorfaceSecurityEvaluator.hasUserId(#id) or hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    fun update(@RequestBody userPatchUpdate: UserPatchUpdate, @PathVariable id: UUID): ResponseEntity<*> {
        accountFacade.update(id, userPatchUpdate)
        return ResponseEntity.ok().build<Any>()
    }

//    @PostMapping("/confirm")
//    fun confirm(@RequestBody token: ConfirmEmail?): UserConfirm {
//        return signupFacade.confirm(token)
//    }

    @PostMapping("/locale")
    fun changeLocale(@RequestParam lang: String?): ResponseEntity<*> {
        return ResponseEntity.ok<Map<String, Locale>>(java.util.Map.of("language", LocaleContextHolder.getLocale()))
    }

    @PostMapping("/confirm/resend")
    fun resendConfirmEmail(@RequestBody resendConfirmEmail: ResendConfirmEmail): UserRegistered {
        return signupEmailFacade.resendConfirmEmail(resendConfirmEmail)
    }

}
