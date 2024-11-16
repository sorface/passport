package by.sorface.passport.web.controllers

import by.sorface.passport.web.exceptions.UserRequestException
import by.sorface.passport.web.facade.accounts.AccountFacade
import by.sorface.passport.web.facade.signup.SignupEmailFacade
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.principals.DefaultPrincipal
import by.sorface.passport.web.records.requests.AccountSignup
import by.sorface.passport.web.records.requests.ConfirmEmail
import by.sorface.passport.web.records.requests.ResendConfirmEmail
import by.sorface.passport.web.records.requests.UserPatchUpdate
import by.sorface.passport.web.records.responses.AccountSignupResponse
import by.sorface.passport.web.records.responses.ProfileRecord
import by.sorface.passport.web.records.responses.UserConfirm
import by.sorface.passport.web.records.responses.UserRegistered
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/accounts")
open class AccountController(private val signupEmailFacade: SignupEmailFacade, private val accountFacade: AccountFacade) {

    @GetMapping(value = ["/current"])
    fun self(): ProfileRecord {
        val principal: DefaultPrincipal = getAuthenticatedUser()
        return accountFacade.getCurrent(principal.id!!)
    }

    @PostMapping(value = ["/signup"])
    fun signupWithSignIn(@RequestBody accountSignup: AccountSignup, request: HttpServletRequest, response: HttpServletResponse?): AccountSignupResponse {
        signupEmailFacade.signup(accountSignup)

        try {
            request.login(accountSignup.email, accountSignup.password)
        } catch (e: ServletException) {
            throw UserRequestException(I18Codes.I18UserCodes.ALREADY_AUTHENTICATED)
        }

        val auth = request.userPrincipal as Authentication

        val user: DefaultPrincipal = auth.principal as DefaultPrincipal

        return AccountSignupResponse(user.username, accountSignup.email)
    }

    @PreAuthorize("#id == authentication.principal.id or hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    fun update(@RequestBody userPatchUpdate: UserPatchUpdate, @PathVariable id: UUID): ResponseEntity<*> {
        accountFacade.update(id, userPatchUpdate)
        return ResponseEntity.ok().build<Any>()
    }
//
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

    private fun getAuthenticatedUser(): DefaultPrincipal {
        val auth: Authentication = SecurityContextHolder.getContext().getAuthentication()

        if (Objects.isNull(auth) || auth is AnonymousAuthenticationToken) {
            throw AccessDeniedException(I18Codes.I18GlobalCodes.ACCESS_DENIED)
        }

        val principal: DefaultPrincipal = auth.principal as DefaultPrincipal

        if (Objects.isNull(principal)) {
            throw AccessDeniedException(I18Codes.I18GlobalCodes.ACCESS_DENIED)
        }

        return principal
    }
}
