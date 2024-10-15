package by.sorface.passport.web.facade.emails;

import by.sorface.passport.web.config.options.OAuth2Options;
import by.sorface.passport.web.records.I18Codes;
import by.sorface.passport.web.records.mails.MailImage;
import by.sorface.passport.web.records.mails.MailTemplate;
import by.sorface.passport.web.services.emails.EmailService;
import by.sorface.passport.web.services.locale.LocaleI18Service;
import by.sorface.passport.web.utils.json.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailLocaleMessageFacade {

    private final LocaleI18Service localeI18Service;

    private final EmailService emailService;

    private final OAuth2Options oAuth2Options;

    @Async
    public void sendConfirmRegistry(final Locale locale, final String recipient, final String hash) {
        LocaleContextHolder.setLocale(locale);

        final var context = new Context();
        {
            context.setVariable("token", hash);
            context.setVariable("issuer", oAuth2Options.getIssuerUrl());
        }

        final var emailTemplate = localeI18Service.getI18Message(I18Codes.I18EmailCodes.TEMPLATE);
        final var subject = localeI18Service.getI18Message(I18Codes.I18EmailCodes.CONFIRMATION_REGISTRATION);

        final var images = List.of(new MailImage("isorface.png"));

        final var mailTemplate = new MailTemplate(recipient, subject, emailTemplate, context, images);

        log.info("Preparing an email to confirm the account");
        log.debug("{}{}", System.lineSeparator(), Json.stringify(mailTemplate));

        emailService.sendHtml(mailTemplate);

        log.info("The email to confirm your account has been sent to {}", recipient);

    }

    @Async
    public void sendRenewPasswordEmail(final Locale locale, final String recipient, final String hash, final String username) {
        LocaleContextHolder.setLocale(locale);

        final var context = new Context();
        {
            context.setVariable("username", username);
            context.setVariable("token", hash);
            context.setVariable("issuer", oAuth2Options.getIssuerUrl());
        }

        final var emailTemplate = localeI18Service.getI18Message(I18Codes.I18EmailCodes.TEMPLATE_RENEW_PASSWORD);
        final var subject = localeI18Service.getI18Message(I18Codes.I18EmailCodes.SUBJECT_TITLE_RESET_PASSWORD);

        final var images = List.of(new MailImage("isorface.png"));

        final var mailTemplate = new MailTemplate(recipient, subject, emailTemplate, context, images);

        emailService.sendHtml(mailTemplate);
    }

}
