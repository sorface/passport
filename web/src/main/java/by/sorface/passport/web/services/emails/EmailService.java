package by.sorface.passport.web.services.emails;

import by.sorface.passport.web.records.mails.Mail;
import by.sorface.passport.web.records.mails.MailTemplate;

/**
 * Interface for EmailService.
 * This interface provides methods for sending emails.
 */
public interface EmailService {

    /**
     * Sends an email.
     *
     * @param mail The Mail object containing the email details.
     */
    void send(final Mail mail);

    /**
     * Sends an HTML email.
     * @param mailTemplate The MailTemplate object containing the HTML email details.
     */
    void sendHtml(final MailTemplate mailTemplate);
}
