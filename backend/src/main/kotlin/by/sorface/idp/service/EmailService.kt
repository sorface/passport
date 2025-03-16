package by.sorface.idp.service

import by.sorface.idp.records.Mail
import by.sorface.idp.records.MailTemplate

/**
 * Интерфейс EmailService предоставляет методы для отправки электронных писем.
 */
interface EmailService {

    /**
     * Отправляет электронное письмо.
     *
     * @param mail объект Mail, содержащий информацию о письме.
     */
    fun send(mail: Mail)

    /**
     * Отправляет электронное письмо в формате HTML.
     *
     * @param mailTemplate объект MailTemplate, содержащий информацию о шаблоне письма.
     */
    fun sendHtml(mailTemplate: MailTemplate)

}
