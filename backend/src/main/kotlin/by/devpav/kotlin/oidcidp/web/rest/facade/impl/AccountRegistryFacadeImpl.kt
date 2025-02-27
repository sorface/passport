package by.devpav.kotlin.oidcidp.web.rest.facade.impl

import by.devpav.kotlin.oidcidp.dao.nosql.model.TmpRegistration
import by.devpav.kotlin.oidcidp.dao.nosql.repository.TmpRegistrationRepository
import by.devpav.kotlin.oidcidp.dao.sql.repository.user.UserRepository
import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.utils.OtpUtils
import by.devpav.kotlin.oidcidp.web.rest.exceptions.I18RestException
import by.devpav.kotlin.oidcidp.web.rest.facade.AccountRegistryFacade
import by.devpav.kotlin.oidcidp.web.rest.model.accounts.AccountRegistration
import by.devpav.kotlin.oidcidp.web.rest.model.accounts.AccountRegistrationResult
import by.sorface.passport.web.utils.json.mask.MaskerFields
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder

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

        val tmpRegistration = TmpRegistration(
            account.email,
            account.username,
            passwordEncoder.encode(account.password),
            otp = OtpUtils.generateOTP(5)
        ).apply {
            firstName = account.firstName
            lastName = account.lastName
        }

        val accountRegistration = tmpRegistrationRepository.save(tmpRegistration)

        return AccountRegistrationResult(accountRegistration.id)
    }

}