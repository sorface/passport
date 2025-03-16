package by.sorface.idp.service.impl

import by.sorface.idp.service.I18Service
import org.apache.commons.text.StringSubstitutor
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class I18ServiceImpl(private val messageSource: ResourceBundleMessageSource) : I18Service {

    override fun getI18Message(i18Code: String, args: Map<String, Any>, locale: Locale): String? {
        if (i18Code.isNullOrBlank()) {
            return null
        }

        val template: String = messageSource.getMessage(i18Code, null, locale)

        if (args.isEmpty()) {
            return template
        }

        return StringSubstitutor.replace(template, args, "{", "}")
    }

    override fun getI18MessageOrDefault(i18Code: String, args: Map<String, Any>, locale: Locale, defaultCode: String): String =
        runCatching {
            return@runCatching getI18Message(i18Code, args, locale) ?: getI18Message(defaultCode, args, locale)!!
        }
            .getOrElse {
                return@getOrElse getI18Message(defaultCode, args, locale)!!
            }

}
