package by.sorface.passport.web.services.locale

import org.apache.commons.text.StringSubstitutor
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocaleI18Service(private val messageSource: ResourceBundleMessageSource) : LocaleService {

    override fun getI18Message(i18Code: String?, args: Map<String, String>, locale: Locale): String? {
        if (i18Code.isNullOrBlank()) {
            return null
        }

        val template = messageSource.getMessage(i18Code, null, locale)

        if (args.isEmpty()) {
            return template
        }

        return StringSubstitutor.replace(template, args, "{", "}")
    }

}
