package by.sorface.passport.web.facade.signup

import by.sorface.passport.web.config.AccountRegistryKeyspace
import by.sorface.passport.web.config.AccountRegistryOneTimePasswordKeyspace
import by.sorface.passport.web.dao.nosql.redis.models.AccountRegistry
import by.sorface.passport.web.dao.nosql.redis.models.OneTimePassword
import by.sorface.passport.web.dao.nosql.redis.repository.RedisAccountRegistryOneTimePasswordRepository
import by.sorface.passport.web.dao.nosql.redis.repository.RedisAccountRegistryRepository
import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.dao.sql.models.enums.ProviderType
import by.sorface.passport.web.exceptions.UserRequestException
import by.sorface.passport.web.extensions.toStringMask
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.mails.MailTemplate
import by.sorface.passport.web.services.emails.EmailService
import by.sorface.passport.web.services.locale.LocaleI18Service
import by.sorface.passport.web.services.users.RoleService
import by.sorface.passport.web.services.users.UserService
import by.sorface.passport.web.utils.json.mask.MaskerFields
import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.context.Context
import java.time.Instant
import java.util.*
import java.util.function.Supplier

@Service
class OneTimePasswordService(private val redisAccountRegistryOneTimePasswordRepository: RedisAccountRegistryOneTimePasswordRepository) {

    fun save(code: String): OneTimePassword = OneTimePassword(code).also { redisAccountRegistryOneTimePasswordRepository.save(it) }

    fun <E : Throwable> findByIdOrThrow(id: String, throwingConsumer: Supplier<E>): OneTimePassword =
        redisAccountRegistryOneTimePasswordRepository.findByIdOrNull(id) ?: throw throwingConsumer.get()

    fun findByIdOrNull(id: String): OneTimePassword? = redisAccountRegistryOneTimePasswordRepository.findByIdOrNull(id)

    fun deleteById(id: String) = redisAccountRegistryOneTimePasswordRepository.deleteById(id)
}

data class UserRegistration(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
)

data class AccountRegistrationInfo(
    val registrationId: String,
    @JsonIgnore
    val otpId: String,
    @JsonIgnore
    val registryExpiredSeconds: Int,
    val otpExpiredTime: Instant
)

data class OtpRefreshed(
    val otpExpiredTime: Instant
)

data class ConfirmAccount(val code: String)

@Service
class AccountRegistryService(
    private val oneTimePasswordService: OneTimePasswordService,
    private val redisAccountRegistryRepository: RedisAccountRegistryRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserService,
    private val emailService: EmailService,
    private val i18Service: LocaleI18Service,
    private val roleService: RoleService,
    private val accountRegistryKeyspace: AccountRegistryKeyspace,
    private val accountRegistryOneTimePasswordKeyspace: AccountRegistryOneTimePasswordKeyspace
) {

    private val logger: Logger = LoggerFactory.getLogger(AccountRegistryService::class.java)

    fun registry(user: UserRegistration): AccountRegistrationInfo {
        logger.info("registration of a new account via an OTP code with username [${user.username}] and email [${user.email.toStringMask(MaskerFields.EMAILS)}]")

        if (userService.isExistUsername(user.username)) {
            throw UserRequestException(I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_LOGIN)
        }

        if (userService.isExistEmail(user.email)) {
            throw UserRequestException(I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_EMAIL)
        }

        val otp = oneTimePasswordService.save(generateOtp())

        logger.info("generated OTP [id -> ${otp.id}] for new account with username [${user.username}] and email [${user.email.toStringMask(MaskerFields.EMAILS)}]")

        val newProfileTemplate = AccountRegistry(user.email, user.username, passwordEncoder.encode(user.password), otp.id)
            .apply {
                firstName = user.firstName
                lastName = user.lastName
            }

        val newProfile = redisAccountRegistryRepository.save(newProfileTemplate)

        logger.info("temporary data for the new account is saved to cache with the ID [${newProfile.id}] and OTP ID [${newProfile.otpId}]")

        val locale = LocaleContextHolder.getLocale()

        CoroutineScope(Dispatchers.IO).launch {
            sendOtpCodeToEmailAsync(user.email, otp.code, locale = locale)
        }

        logger.info("forming a response to the operation of creating a new account with ID [${newProfile.id}] and OTP ID [${newProfile.otpId}]")

        return AccountRegistrationInfo(
            newProfile.id,
            otp.id,
            accountRegistryKeyspace.lifeTime.toSeconds().toInt(),
            Instant.now().plusSeconds(accountRegistryOneTimePasswordKeyspace.lifeTime.toSeconds())
        )
    }

    @Transactional
    fun confirm(registrationId: String, confirmAccount: ConfirmAccount) {
        val accountRegistry = redisAccountRegistryRepository.findByIdOrNull(registrationId)
            ?: throw UserRequestException(I18Codes.I18OtpCodes.EXPIRED_CODE)

        if (userService.isExistUsername(accountRegistry.username)) {
            throw UserRequestException(I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_LOGIN)
        }

        if (userService.isExistEmail(accountRegistry.email)) {
            throw UserRequestException(I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_EMAIL)
        }

        val otp = oneTimePasswordService.findByIdOrThrow(accountRegistry.otpId) { UserRequestException(I18Codes.I18OtpCodes.INVALID_CODE) }
            .takeIf { it.code == confirmAccount.code }
            ?: throw UserRequestException(I18Codes.I18OtpCodes.INVALID_CODE)

        val userRole = roleService.findByValue("USER")

        val user = UserEntity().apply {
            email = accountRegistry.email
            username = accountRegistry.username
            password = accountRegistry.password
            firstName = accountRegistry.firstName
            lastName = accountRegistry.lastName
            enabled = true
            confirm = true
            roles = listOf(userRole!!)
            providerType = ProviderType.INTERNAL
        }

        userService.save(user)

        redisAccountRegistryRepository.deleteById(accountRegistry.id)

        val locale = LocaleContextHolder.getLocale()

        CoroutineScope(Dispatchers.IO).launch {
            sendEmailSuccessRegistrationAsync(accountRegistry.email, accountRegistry.username, locale = locale)
        }

        oneTimePasswordService.deleteById(accountRegistry.otpId)
    }

    fun updateOtp(registrationId: String): OtpRefreshed {
        logger.info("update otp for account registration $registrationId")

        val accountRegistry =
            redisAccountRegistryRepository.findByIdOrNull(registrationId) ?: throw UserRequestException(I18Codes.I18AccountRegistryCodes.ACCOUNT_DATA_NOT_FOUND)

        val otp = oneTimePasswordService.findByIdOrNull(accountRegistry.otpId)

        if (otp != null) {
            oneTimePasswordService.deleteById(accountRegistry.otpId)
        }

        val newOtp = oneTimePasswordService.save(generateOtp())

        logger.info("created new otp with id [${newOtp.id}] for account registration id [$registrationId]")

        accountRegistry.otpId = newOtp.id

        redisAccountRegistryRepository.save(accountRegistry)

        val locale = LocaleContextHolder.getLocale()

        CoroutineScope(Dispatchers.IO).launch {
            sendOtpCodeToEmailAsync(accountRegistry.email, newOtp.code, locale = locale)
        }

        return OtpRefreshed(Instant.now().plusSeconds(accountRegistryOneTimePasswordKeyspace.lifeTime.toSeconds()))
    }

    private suspend fun sendOtpCodeToEmailAsync(email: String, otpCode: String, locale: Locale) {
        val context = Context().apply { setVariable("otp", otpCode) }

        val emailTemplate = i18Service.getI18Message(I18Codes.I18EmailCodes.HTML_TEMPLATE_OTP, locale = locale)!!
        val subject = i18Service.getI18Message(I18Codes.I18EmailCodes.CONFIRMATION_REGISTRATION, locale = locale)!!

        val mailTemplate = MailTemplate(email, subject, emailTemplate, context)

        logger.info("preparing an email [${email.toStringMask(MaskerFields.EMAILS)}] to confirm the account by OTP code [${otpCode.toStringMask(maskingType = MaskerFields.TOKEN)}]")

        emailService.sendHtml(mailTemplate)

        logger.info("the email to confirm your account has been sent to ${email.toStringMask(MaskerFields.EMAILS)}")
    }

    private suspend fun sendEmailSuccessRegistrationAsync(email: String, username: String, locale: Locale) {
        val context = Context().apply { setVariable("username", username) }

        val emailTemplate = i18Service.getI18Message(I18Codes.I18EmailCodes.HTML_TEMPLATE_SUCCESS_REGISTRATION, locale = locale)!!
        val subject = i18Service.getI18Message(I18Codes.I18EmailCodes.SUBJECT_SUCCESS_REGISTRATION, locale = locale)!!

        val mailTemplate = MailTemplate(email, subject, emailTemplate, context)

        logger.info(
            "Sending an html email about the successful completion of registration for user with username $username " +
                    "to email address [${email.toStringMask(MaskerFields.EMAILS)}]"
        )

        emailService.sendHtml(mailTemplate)

        logger.info(
            "html email about successful registration has been successfully sent to the user with username [$username] " +
                    "to email address [${email.toStringMask(MaskerFields.EMAILS)}]"
        )
    }

    fun generateOtp(): String {
        return (0..5).joinToString(separator = "") { (1 until 10).random().toString() }
    }
}