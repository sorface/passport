package by.sorface.idp.records

import org.thymeleaf.context.Context

data class MailTemplate(val to: String, val subject: String, val template: String, val context: Context, val images: List<MailImage> = listOf())
