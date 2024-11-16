package by.sorface.passport.web.services.emails

import by.sorface.passport.web.records.mails.Mail
import by.sorface.passport.web.records.mails.MailImage
import by.sorface.passport.web.records.mails.MailTemplate
import jakarta.mail.MessagingException
import liquibase.pro.packaged.e
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import java.util.*
import java.util.function.Consumer

@Service
class DefaultEmailService(private val templateEngine: TemplateEngine, private val emailSender: JavaMailSender) : EmailService {

    @Value("\${spring.mail.username}")
    private lateinit var sender: String

    override fun send(mail: Mail) {
        val message = SimpleMailMessage()

        run {
            message.from = sender
            message.setTo(mail.to)
            message.subject = mail.subject
            message.text = mail.body
            message.sentDate = Date()
        }

        emailSender.send(message)
    }

    override fun sendHtml(mailTemplate: MailTemplate) {
        try {
            val mimeMessage = emailSender.createMimeMessage()
            val email = MimeMessageHelper(mimeMessage, true, "UTF-8")

            val html = templateEngine.process(mailTemplate.template + ".html", mailTemplate.context)

            email.setFrom(sender)
            email.setTo(mailTemplate.to)
            email.setText(html, true)
            email.setSentDate(Date())

            (mailTemplate.images ?: listOf())
                .forEach { image: MailImage ->
                    val simpleImageName = image.name.replaceFirst("[.][^.]+$".toRegex(), "")
                    val path = buildImageResourcePath(image.name)
                    val clr = ClassPathResource(path)
                    try {
                        email.addInline(simpleImageName, clr)
                    } catch (e: MessagingException) {
                        logger.warn("Failed to add inline image ${image.name}", e)
                    }
                }

            mimeMessage.subject = mailTemplate.subject

            emailSender.send(mimeMessage)
        } catch (e: MessagingException) {
            logger.warn("Failed to send email to ${mailTemplate.to} with subject ${mailTemplate.subject}", e)
        }
    }

    private fun buildImageResourcePath(imageFileName: String): String {
        return CLASSPATH_STATIC_IMAGES + imageFileName
    }

    companion object {
        const val CLASSPATH_STATIC_IMAGES: String = "/static/images/"

        private val logger: Logger = LoggerFactory.getLogger(DefaultEmailService::class.java)
    }
}