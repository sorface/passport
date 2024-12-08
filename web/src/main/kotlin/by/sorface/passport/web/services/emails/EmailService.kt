package by.sorface.passport.web.services.emails

import by.sorface.passport.web.records.mails.Mail
import by.sorface.passport.web.records.mails.MailTemplate


interface EmailService {

    fun send(mail: Mail)

    fun sendHtml(mailTemplate: MailTemplate)

}
