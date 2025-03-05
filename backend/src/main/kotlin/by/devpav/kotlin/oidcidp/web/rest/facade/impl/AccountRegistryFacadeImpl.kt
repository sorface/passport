package by.devpav.kotlin.oidcidp.web.rest.facade.impl

import by.devpav.kotlin.oidcidp.dao.nosql.model.TmpRegistration
import by.devpav.kotlin.oidcidp.dao.nosql.repository.TmpRegistrationRepository
import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.user.UserRepository
import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.utils.OtpUtils
import by.devpav.kotlin.oidcidp.web.rest.exceptions.I18RestException
import by.devpav.kotlin.oidcidp.web.rest.facade.AccountRegistryFacade
import by.devpav.kotlin.oidcidp.web.rest.model.accounts.registration.AccountOtpRefresh
import by.devpav.kotlin.oidcidp.web.rest.model.accounts.registration.AccountRegistration
import by.devpav.kotlin.oidcidp.web.rest.model.accounts.registration.AccountRegistrationResult
import by.sorface.passport.web.utils.json.mask.MaskerFields
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AccountRegistryFacadeImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tmpRegistrationRepository: TmpRegistrationRepository
) : AccountRegistryFacade {

    private val logger = LoggerFactory.getLogger(AccountRegistryFacadeImpl::class.java)

    override fun registry(account: AccountRegistration): AccountRegistrationResult {
        logger.info(
            "registration of a new account via an OTP code with username [${account.username}] and email " +
                    "[${MaskerFields.EMAILS.mask(account.email)}]"
        )

        if (userRepository.existsByUsername(account.username)) {
            throw I18RestException(
                message = "Username ${account.username} already exists with login",
                i18Code = I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_LOGIN,
                httpStatus = HttpStatus.BAD_REQUEST
            )
        }

        if (userRepository.existsByEmail(account.email)) {
            throw I18RestException(
                message = "Username ${account.username} already exists with email",
                i18Code = I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_EMAIL,
                httpStatus = HttpStatus.BAD_REQUEST
            )
        }

        var tmpRegistration: TmpRegistration? = null

        val registrationId = account.registrationId

        if (registrationId != null) {
            val registration = tmpRegistrationRepository.findByIdOrNull(registrationId)

            tmpRegistration = registration?.apply {
                this.firstName = account.firstName
                this.lastName = account.lastName
                this.email = account.email
                this.username = account.username
            }
        }

        if (tmpRegistration == null) {
            tmpRegistration = TmpRegistration(
                account.email,
                account.username,
                passwordEncoder.encode(account.password),
                otp = OtpUtils.generateOTP(5)
            )
                .apply {
                    this.firstName = account.firstName
                    this.lastName = account.lastName
                    this.otpExpTime = Instant.now().plusSeconds(120)
                }
        }

        return tmpRegistration
            .let { tmpRegistrationRepository.save(it) }
            .let { AccountRegistrationResult(it.id, it.otpExpTime) }
    }

    @Transactional
    override fun confirmByOtp(registrationId: String, otpCode: String): Boolean {
        val registration = tmpRegistrationRepository.findByIdOrNull(registrationId)
            ?: throw I18RestException(
                message = "Registration with id $registrationId does not exist",
                i18Code = I18Codes.I18AccountRegistryCodes.ACCOUNT_DATA_NOT_FOUND
            )

        val now = Instant.now()

        if (registration.otpExpTime.isBefore(now)) {
            throw I18RestException(
                message = "OTP has expired. [exp time -> ${registration.otpExpTime}, now -> ${now}]",
                i18Code = I18Codes.I18OtpCodes.EXPIRED_CODE
            )
        }

        if (registration.otp != otpCode) {
            throw I18RestException(
                message = "OTP has not matching code",
                i18Code = I18Codes.I18OtpCodes.INVALID_CODE
            )
        }

        if (userRepository.existsByUsername(registration.username)) {
            throw I18RestException(
                message = "Username already exist with login ${registration.username}",
                i18Code = I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_LOGIN
            )
        }

        if (userRepository.existsByEmail(registration.email)) {
            throw I18RestException(
                message = "Username already exist with email ${registration.username}",
                i18Code = I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_EMAIL
            )
        }

        val newAccount = UserModel().apply {
            this.username = registration.username
            this.email = registration.email
            this.firstName = registration.firstName
            this.lastName = registration.lastName
            this.password = registration.password
        }

        userRepository.save(newAccount)

        return true;
    }

    override fun refreshOtp(registrationId: String): AccountOtpRefresh {
        val registration = tmpRegistrationRepository.findByIdOrNull(registrationId)
            ?: throw I18RestException(
                message = "Registration with id $registrationId does not exist",
                i18Code = I18Codes.I18AccountRegistryCodes.ACCOUNT_DATA_NOT_FOUND
            )

        return registration
            .apply {
                this.otp = OtpUtils.generateOTP(5)
                this.otpExpTime = Instant.now().plusSeconds(120)
            }.let {
                AccountOtpRefresh(it.otpExpTime)
            }
    }

}