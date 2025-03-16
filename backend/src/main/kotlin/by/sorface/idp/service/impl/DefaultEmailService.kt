package by.sorface.idp.service.impl

import by.sorface.idp.records.Mail
import by.sorface.idp.records.MailImage
import by.sorface.idp.records.MailTemplate
import by.sorface.idp.service.EmailService
import jakarta.mail.MessagingException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import java.util.*

@Service
class DefaultEmailService(private val templateEngine: TemplateEngine, private val emailSender: JavaMailSender) : EmailService {

    @Autowired
    private lateinit var mailProperties: MailProperties

    override fun send(mail: Mail) {
        val message = SimpleMailMessage()

        message.from = mailProperties.username
        message.setTo(mail.to)
        message.subject = mail.subject
        message.text = mail.body
        message.sentDate = Date()

        emailSender.send(message)
    }

    override fun sendHtml(mailTemplate: MailTemplate) {
        try {
            val mimeMessage = emailSender.createMimeMessage()
            val email = MimeMessageHelper(mimeMessage, true, "UTF-8")

            val html = templateEngine.process(mailTemplate.template + ".html", mailTemplate.context)

            email.setFrom(mailProperties.username)
            email.setTo(mailTemplate.to)
            email.setText(html, true)
            email.setSentDate(Date())

            mailTemplate.images
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