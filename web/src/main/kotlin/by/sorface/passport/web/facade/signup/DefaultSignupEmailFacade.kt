package by.sorface.passport.web.facade.signup

import by.sorface.passport.web.config.options.OAuth2Options
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.mails.MailImage
import by.sorface.passport.web.records.mails.MailTemplate
import by.sorface.passport.web.records.requests.AccountSignup
import by.sorface.passport.web.records.requests.ResendConfirmEmail
import by.sorface.passport.web.records.responses.UserRegistered
import by.sorface.passport.web.records.responses.UserRegisteredHash
import by.sorface.passport.web.services.emails.EmailService
import by.sorface.passport.web.services.locale.LocaleI18Service
import by.sorface.passport.web.utils.json.Json.lazyStringifyWithMasking
import by.sorface.passport.web.utils.json.Json.stringify
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context

@Service
class DefaultSignupEmailFacade(
    private val emailService: EmailService,
    private val userRegistryFacade: DefaultSignupFacade,
    private val oAuth2Options: OAuth2Options,
    private val localeI18Service: LocaleI18Service
) : SignupEmailFacade {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DefaultSignupEmailFacade::class.java)
    }

    override fun signup(user: AccountSignup): UserRegistered {
        LOGGER.info("User registration request received {}{}", System.lineSeparator(), lazyStringifyWithMasking(user))

        val registry = userRegistryFacade.signup(user)

        LOGGER.info("User registration completed. [account id - {}]", registry.id)

        this.sendRegistryEmail(registry)

        return UserRegistered(registry.id, registry.email)
    }

    /**
     * @param email user's email
     * @return user info
     */
    override fun resendConfirmEmail(email: ResendConfirmEmail): UserRegistered {
        val registry = userRegistryFacade.findTokenByEmail(email.email)

        this.sendRegistryEmail(registry)

        return UserRegistered(registry.id, registry.email)
    }

    /**
     * Sending an email with a confirmation token
     *
     * @param registeredUser user information
     */
    private fun sendRegistryEmail(registeredUser: UserRegisteredHash?) {
        val context = Context()
        run {
            context.setVariable("token", registeredUser!!.hash)
            context.setVariable("issuer", oAuth2Options.issuerUrl)
        }

        val emailTemplate = localeI18Service.getI18Message(I18Codes.I18EmailCodes.TEMPLATE)
        val subject = localeI18Service.getI18Message(I18Codes.I18EmailCodes.CONFIRMATION_REGISTRATION)

        val images = listOf(MailImage("isorface.png"))

        val mailTemplate = MailTemplate(registeredUser!!.email, subject, emailTemplate, context, images)

        LOGGER.info("Preparing an email to confirm the account")
        LOGGER.debug("{}{}", System.lineSeparator(), stringify(mailTemplate))

        emailService.sendHtml(mailTemplate)

        LOGGER.info("The email to confirm your account has been sent to {}", registeredUser.email)
    }
}
