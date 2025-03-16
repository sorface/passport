package by.sorface.idp.service.listeners

import by.sorface.idp.records.I18Codes
import by.sorface.idp.records.MailTemplate
import by.sorface.idp.service.EmailService
import by.sorface.idp.service.I18Service
import by.sorface.idp.web.rest.facade.impl.AccountRegistrationConfirmEvent
import by.sorface.idp.web.rest.facade.impl.AccountRegistrationEvent
import by.sorface.idp.web.rest.facade.impl.AccountRegistrationRefreshOtpEvent
import by.sorface.passport.web.utils.json.mask.MaskerFields
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context

@Service
class RegistrationEventListener(private val i18Service: I18Service, private val emailService: EmailService) {

    private val logger: Logger = LoggerFactory.getLogger(RegistrationEventListener::class.java)

    @Async
    @EventListener
    fun created(event: AccountRegistrationEvent) {
        logger.info("Received account registration event")

        val context = Context().apply { setVariable("otp", event.otpCode) }

        val emailTemplate = i18Service.getI18Message(I18Codes.I18EmailCodes.HTML_TEMPLATE_OTP, locale = event.locale)!!
        val subject = i18Service.getI18Message(I18Codes.I18EmailCodes.CONFIRMATION_REGISTRATION, locale = event.locale)!!

        val mailTemplate = MailTemplate(event.email, subject, emailTemplate, context)

        logger.info("preparing an email [${MaskerFields.EMAILS.mask(event.email)}] to confirm the account by OTP code [${MaskerFields.TOKEN.mask(event.otpCode)}]")

        emailService.sendHtml(mailTemplate)

        logger.info("the email to confirm your account has been sent to ${MaskerFields.EMAILS.mask(event.email)}")
    }

    @Async
    @EventListener
    fun confirm(event: AccountRegistrationConfirmEvent) {
        val context = Context().apply { setVariable("username", event.username) }

        val emailTemplate = i18Service.getI18Message(I18Codes.I18EmailCodes.HTML_TEMPLATE_SUCCESS_REGISTRATION, locale = event.locale)!!
        val subject = i18Service.getI18Message(I18Codes.I18EmailCodes.SUBJECT_SUCCESS_REGISTRATION, locale = event.locale)!!

        val mailTemplate = MailTemplate(event.email, subject, emailTemplate, context)

        logger.info(
            "Sending an html email about the successful completion of registration for user with username ${event.username} " +
                    "to email address [${MaskerFields.EMAILS.mask(event.email)}]"
        )

        emailService.sendHtml(mailTemplate)

        logger.info(
            "html email about successful registration has been successfully sent to the user with username [${event.username}] " +
                    "to email address [${MaskerFields.EMAILS.mask(event.email)}]"
        )
    }

    @Async
    @EventListener
    fun refreshOtp(event: AccountRegistrationRefreshOtpEvent) {
        val context = Context().apply { setVariable("otp", event) }

        val emailTemplate = i18Service.getI18Message(I18Codes.I18EmailCodes.HTML_TEMPLATE_OTP, locale = event.locale)!!
        val subject = i18Service.getI18Message(I18Codes.I18EmailCodes.CONFIRMATION_REGISTRATION, locale = event.locale)!!

        val mailTemplate = MailTemplate(event.email, subject, emailTemplate, context)

        logger.info("preparing an email [${MaskerFields.EMAILS.mask(event.email)}] to confirm the account by OTP code [${MaskerFields.TOKEN.mask(event.otpCode)}]")

        emailService.sendHtml(mailTemplate)

        logger.info("the email to confirm your account has been sent to ${MaskerFields.EMAILS.mask(event.email)}")

    }

}