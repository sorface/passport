package by.sorface.idp.web.rest.controller

import by.sorface.idp.extencions.findCookieByName
import by.sorface.idp.extencions.findCookieValueByName
import by.sorface.idp.records.I18Codes
import by.sorface.idp.web.rest.exceptions.I18RestException
import by.sorface.idp.web.rest.facade.RegistrationFacade
import by.sorface.idp.web.rest.facade.impl.RegistrationFacadeImpl
import by.sorface.idp.service.AccountCookieManager
import by.sorface.idp.web.rest.model.accounts.registration.AccountOtpConfirm
import by.sorface.idp.web.rest.model.accounts.registration.AccountOtpRefresh
import by.sorface.idp.web.rest.model.accounts.registration.AccountRegistration
import by.sorface.idp.web.rest.model.accounts.registration.AccountRegistrationResult
import by.sorface.passport.web.utils.json.mask.MaskerFields
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

private const val REGISTRATION_COOKIE_NAME = "registrationId"
private const val OTP_EXP_TIME_COOKIE_NAME = "otp_exp_time"

@RestController
@RequestMapping("/api/registrations")
@PreAuthorize("isAnonymous()")
class RegistryAccountController(
    private val registrationFacade: RegistrationFacade,
    private val accountCookieManager: AccountCookieManager
) {

    private val logger = LoggerFactory.getLogger(RegistrationFacadeImpl::class.java)

    @GetMapping
    fun getRegistrationTmp(request: HttpServletRequest): ResponseEntity<AccountRegistration> {
        val registrationId = request.findCookieValueByName(REGISTRATION_COOKIE_NAME)

        registrationId ?: return ResponseEntity.noContent().build()

        logger.info("getting account registration information for registrationId: $registrationId")

        val accountRegistration = registrationFacade.get(registrationId)

        logger.debug("account registration information retrieved for registrationId: $registrationId")

        return ResponseEntity.ok(accountRegistration)
    }

    @PostMapping
    fun signup(@RequestBody userRegistration: @Valid AccountRegistration, request: HttpServletRequest, response: HttpServletResponse): AccountRegistrationResult {
        userRegistration.registrationId = request.findCookieValueByName(REGISTRATION_COOKIE_NAME)

        logger.info("received signup request for user [email -> ${MaskerFields.EMAILS.mask(userRegistration.email)}, username -> ${userRegistration.username}]")

        val accountRegistrationResult = registrationFacade.create(userRegistration)

        logger.info("signup request processed successfully for user [email -> ${MaskerFields.EMAILS.mask(userRegistration.email)}, username -> ${userRegistration.username}]")

        accountRegistrationResult.apply {
            logger.debug("setting a cookie with the registration id [registration id -> ${this.registrationId}]")

            val registrationIdCookie = accountCookieManager.createRegistrationId(this.registrationId)

            response.addCookie(registrationIdCookie)

            logger.debug("setting a cookie with the otp exp time [otp exp time -> {} registration id -> {}]", otpExpiredTime, this.registrationId)

            val otpExpiredAtCookie = accountCookieManager.createOtpExpiredAt(this.otpExpiredTime)

            response.addCookie(otpExpiredAtCookie)
        }

        return accountRegistrationResult
    }

    @PostMapping(value = ["/confirm"])
    fun confirmSignup(@RequestBody confirmAccount: AccountOtpConfirm, request: HttpServletRequest, response: HttpServletResponse): Map<String, Any> {
        val registrationCookie = request.findCookieByName(REGISTRATION_COOKIE_NAME)
            ?: throw I18RestException(
                message = "Cookie with name [registrationId] is not present in the request",
                i18Code = I18Codes.I18AccountRegistryCodes.ACCOUNT_DATA_NOT_FOUND
            )

        val registrationId = registrationCookie.value

        logger.info("received confirm registration request for registration [id -> $registrationId]")

        registrationFacade.confirm(registrationId, confirmAccount.code)

        val dropRegistrationCookie = registrationCookie.apply {
            maxAge = 0
            path = "/"
            isHttpOnly = true
        }

        logger.debug("drop cookie registration [id -> {}]", registrationId)

        response.addCookie(dropRegistrationCookie)

        val dropOtpExpTimeCookie = request.findCookieByName(OTP_EXP_TIME_COOKIE_NAME)

        if (dropOtpExpTimeCookie != null) {
            dropOtpExpTimeCookie.apply {
                maxAge = 0
                path = "/"
                isHttpOnly = true
            }

            logger.debug("drop cookie otp exp time [otp exp time -> {}, registration id -> {}]", dropOtpExpTimeCookie.value, registrationId)

            response.addCookie(dropOtpExpTimeCookie)
        }

        logger.info("confirm request processed successfully for registration [id -> $registrationId]")

        return mapOf("confirm" to true)
    }

    @PutMapping(value = ["/otp"])
    fun updateOtp(request: HttpServletRequest, response: HttpServletResponse): AccountOtpRefresh {
        val registrationId = request.findCookieValueByName(REGISTRATION_COOKIE_NAME)
            ?: throw I18RestException(
                message = "Cookie with name [registrationId] is not present in the request",
                i18Code = I18Codes.I18AccountRegistryCodes.ACCOUNT_DATA_NOT_FOUND
            )

        logger.info("received refresh otp request for registration [id -> $registrationId]")

        val refreshOtp = registrationFacade.refreshOtp(registrationId)

        logger.info("refresh otp request processed successfully for registration [id -> $registrationId]")

        val otpExpiredAtCookie = accountCookieManager.createOtpExpiredAt(refreshOtp.otpExpTime)

        logger.debug("setting a cookie with the new otp exp time [otp exp time -> {}, registration id -> {}]", otpExpiredAtCookie.value, registrationId)

        response.addCookie(otpExpiredAtCookie)

        return refreshOtp
    }

}
