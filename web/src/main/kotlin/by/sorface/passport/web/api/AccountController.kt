package by.sorface.passport.web.api

import by.sorface.passport.web.exceptions.UserRequestException
import by.sorface.passport.web.facade.accounts.AccountFacade
import by.sorface.passport.web.facade.signup.*
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.requests.UserPatchUpdate
import by.sorface.passport.web.records.responses.ProfileRecord
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountFacade: AccountFacade,
    private val accountRegistryService: AccountRegistryService,
    private val accountCookieBuilder: (registrationId: String, maxAge: Int) -> Cookie,
    private val accountCookieValue: (request: HttpServletRequest) -> String?
) {

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = ["/current"])
    fun getCurrentAuthorizedUser(): ProfileRecord = accountFacade.getCurrentAuthorizedUser()

    @PostMapping(value = ["/signup"])
    @PreAuthorize("isAnonymous()")
    fun signup(@RequestBody userRegistration: @Valid UserRegistration, response: HttpServletResponse): AccountRegistrationInfo =
        accountRegistryService.registry(userRegistration).apply {
            response.addCookie(accountCookieBuilder.invoke(registrationId.toString(), this.registryExpiredSeconds))
        }

    @PostMapping(value = ["/confirm"])
    @PreAuthorize("isAnonymous()")
    fun confirmSignup(@RequestBody confirmAccount: ConfirmAccount, request: HttpServletRequest) {
        val registrationId = accountCookieValue.invoke(request) ?: throw UserRequestException(I18Codes.I18OtpCodes.INVALID_CODE)
        accountRegistryService.confirm(registrationId, confirmAccount)
    }

    @PutMapping(value = ["/otp"])
    @PreAuthorize("isAnonymous()")
    fun updateOtp(request: HttpServletRequest): OtpRefreshed {
        val registrationId = accountCookieValue.invoke(request) ?: throw UserRequestException(I18Codes.I18AccountRegistryCodes.ACCOUNT_DATA_NOT_FOUND)
        return accountRegistryService.updateOtp(registrationId)
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@advancedSecurityEvaluator.hasPrincipalId(#id) or hasAuthority('ADMIN')")
    fun update(@RequestBody userPatchUpdate: UserPatchUpdate, @PathVariable id: UUID): ProfileRecord = accountFacade.update(id, userPatchUpdate)

}
