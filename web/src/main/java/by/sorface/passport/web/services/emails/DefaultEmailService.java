package by.sorface.passport.web.services.emails;

import by.sorface.passport.web.records.mails.Mail;
import by.sorface.passport.web.records.mails.MailTemplate;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultEmailService implements EmailService {

    public static final String CLASSPATH_STATIC_IMAGES = "/static/images/";

    private final TemplateEngine templateEngine;

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void send(final Mail mail) {
        final var message = new SimpleMailMessage();
        {
            message.setFrom(sender);
            message.setTo(mail.to());
            message.setSubject(mail.subject());
            message.setText(mail.body());
            message.setSentDate(new Date());
        }

        emailSender.send(message);
    }

    public void sendHtml(final MailTemplate mailTemplate) {
        try {
            final var mimeMessage = emailSender.createMimeMessage();
            final var email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            final var html = templateEngine.process(mailTemplate.template() + ".html", mailTemplate.context());

            email.setFrom(sender);
            email.setTo(mailTemplate.to());
            email.setText(html, true);
            email.setSentDate(new Date());

            Optional.ofNullable(mailTemplate.images()).orElse(List.of())
                    .forEach(image -> {
                        final var simpleImageName = image.name().replaceFirst("[.][^.]+$", "");

                        final var path = buildImageResourcePath(image.name());
                        final var clr = new ClassPathResource(path);

                        try {
                            email.addInline(simpleImageName, clr);
                        } catch (MessagingException e) {
                            log.error(e.getMessage(), e);
                        }
                    });

            mimeMessage.setSubject(mailTemplate.subject());

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String buildImageResourcePath(final String imageFileName) {
        return CLASSPATH_STATIC_IMAGES.concat(imageFileName);
    }
}