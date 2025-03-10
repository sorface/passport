package by.devpav.kotlin.oidcidp.service

import by.devpav.kotlin.oidcidp.records.Mail
import by.devpav.kotlin.oidcidp.records.MailTemplate

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
