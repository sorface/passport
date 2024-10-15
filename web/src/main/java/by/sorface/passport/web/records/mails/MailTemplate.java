package by.sorface.passport.web.records.mails;

import org.thymeleaf.context.Context;

import java.util.List;

public record MailTemplate(String to, String subject, String template, Context context, List<MailImage> images) {
}
