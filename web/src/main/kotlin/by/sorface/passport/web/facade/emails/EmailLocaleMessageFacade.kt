package by.sorface.passport.web.facade.emails

import by.sorface.passport.web.exceptions.NotFoundException
import by.sorface.passport.web.extensions.toJson
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.mails.MailImage
import by.sorface.passport.web.records.mails.MailTemplate
import by.sorface.passport.web.services.emails.EmailService
import by.sorface.passport.web.services.locale.LocaleI18Service
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import java.util.*

@Service
class EmailLocaleMessageFacade(private val localeI18Service: LocaleI18Service, private val emailService: EmailService) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EmailLocaleMessageFacade::class.java)
    }

    fun sendConfirmRegistry(locale: Locale, recipient: String, hash: String) {
        LocaleContextHolder.setLocale(locale)

        val context = Context().run {
            this.setVariable("token", hash)

            this
        }

        val emailTemplate = localeI18Service.getI18Message(I18Codes.I18EmailCodes.TEMPLATE) ?: throw NotFoundException(I18Codes.SELF.NOT_FOUND_KEY)
        val subject = localeI18Service.getI18Message(I18Codes.I18EmailCodes.CONFIRMATION_REGISTRATION) ?: throw NotFoundException(I18Codes.SELF.NOT_FOUND_KEY)

        val images = listOf(MailImage("isorface.png"))

        val mailTemplate = MailTemplate(recipient, subject, emailTemplate, context, images)

        LOGGER.info("Preparing an email to confirm the account")
        LOGGER.debug("{}{}", System.lineSeparator(), mailTemplate.toJson(mask = true))

        emailService.sendHtml(mailTemplate)

        LOGGER.info("The email to confirm your account has been sent to {}", recipient)
    }

    fun sendRenewPasswordEmail(locale: Locale?, recipient: String, hash: String?, username: String?) {
        LocaleContextHolder.setLocale(locale)

        val context = Context().run {
            this.setVariable("username", username)
            this.setVariable("token", hash)
            this
        }

        val emailTemplate = localeI18Service.getI18Message(I18Codes.I18EmailCodes.TEMPLATE_RENEW_PASSWORD) ?: throw NotFoundException(I18Codes.SELF.NOT_FOUND_KEY)
        val subject = localeI18Service.getI18Message(I18Codes.I18EmailCodes.SUBJECT_TITLE_RESET_PASSWORD) ?: throw NotFoundException(I18Codes.SELF.NOT_FOUND_KEY)

        val images = listOf(MailImage("isorface.png"))

        val mailTemplate = MailTemplate(recipient, subject, emailTemplate, context, images)

        emailService.sendHtml(mailTemplate)
    }
}
