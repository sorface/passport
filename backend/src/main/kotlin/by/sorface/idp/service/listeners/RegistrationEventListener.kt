package by.sorface.idp.service.listeners

import by.sorface.idp.records.I18Codes
import by.sorface.idp.records.MailTemplate
import by.sorface.idp.service.EmailService
import by.sorface.idp.service.I18Service
import by.sorface.idp.web.rest.facade.impl.AccountRegistrationConfirmEvent
import by.sorface.idp.web.rest.facade.impl.AccountRegistrationEvent
import by.sorface.idp.web.rest.facade.impl.AccountRegistrationRefreshOtpEvent
import by.sorface.idp.utils.json.mask.MaskerFields
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import java.util.Locale

@Service
class RegistrationEventListener(private val i18Service: I18Service, private val emailService: EmailService) {

    private val logger: Logger = LoggerFactory.getLogger(RegistrationEventListener::class.java)

    @Async
    @EventListener
    fun created(event: AccountRegistrationEvent) {
        logger.info("Received account registration event")

        sendOtpEmail(event.otpCode, event.locale, event.email)
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
        logger.info("Received account refresh OTP event")

        sendOtpEmail(event.otpCode, event.locale, event.email)
    }

    /**
     * Отправляет электронное письмо с кодом OTP.
     *
     * @param otpCode Код OTP.
     * @param locale Локаль.
     * @param email Адрес электронной почты получателя.
     */
    private fun sendOtpEmail(otpCode: String, locale: Locale, email: String) {
        val context = Context().apply { setVariable("otp", otpCode) }

        val emailTemplate = i18Service.getI18Message(I18Codes.I18EmailCodes.HTML_TEMPLATE_OTP, locale = locale)!!
        val subject = i18Service.getI18Message(I18Codes.I18EmailCodes.CONFIRMATION_REGISTRATION, locale = locale)!!

        val mailTemplate = MailTemplate(email, subject, emailTemplate, context)

        emailService.sendHtml(mailTemplate)

        logger.info("the email to confirm your account has been sent to ${MaskerFields.EMAILS.mask(email)}")
    }

}