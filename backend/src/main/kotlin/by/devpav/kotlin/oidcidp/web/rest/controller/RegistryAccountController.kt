package by.devpav.kotlin.oidcidp.web.rest.controller

import by.sorface.passport.web.exceptions.UserRequestException
import by.sorface.passport.web.facade.signup.*
import by.sorface.passport.web.records.I18Codes
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
@PreAuthorize("isAnonymous()")
class RegistryAccountController(
    private val accountRegistryService: AccountRegistryService,
    private val accountCookieBuilder: (registrationId: String, maxAge: Int) -> Cookie,
    private val accountCookieValue: (request: HttpServletRequest) -> String?
) {

    @PostMapping(value = ["/signup"])
    fun signup(@RequestBody userRegistration: @Valid UserRegistration, response: HttpServletResponse): AccountRegistrationInfo =
        accountRegistryService.registry(userRegistration).apply {
            response.addCookie(accountCookieBuilder.invoke(registrationId, this.registryExpiredSeconds))
        }

    @PostMapping(value = ["/confirm"])
    fun confirmSignup(@RequestBody confirmAccount: ConfirmAccount, request: HttpServletRequest) =
        (accountCookieValue.invoke(request) ?: throw UserRequestException(I18Codes.I18OtpCodes.INVALID_CODE))
            .let { registrationId ->
                accountRegistryService.confirm(registrationId, confirmAccount)
            }

    @PutMapping(value = ["/otp"])
    fun updateOtp(request: HttpServletRequest): OtpRefreshed =
        (accountCookieValue.invoke(request) ?: throw UserRequestException(I18Codes.I18AccountRegistryCodes.ACCOUNT_DATA_NOT_FOUND))
            .let { registrationId ->
                accountRegistryService.updateOtp(registrationId)
            }

}
