package by.sorface.passport.web.services.locale

import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.apache.commons.text.StringSubstitutor
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Slf4j
@Service
@RequiredArgsConstructor
class LocaleI18Service(private val messageSource: ResourceBundleMessageSource) : LocaleService {

    override fun getI18Message(i18Code: String): String? = getMessage(i18Code, java.util.Map.of())

    override fun getI18Message(i18Code: String, args: Map<String, String>): String? = getMessage(i18Code, args)

    private fun getMessage(i18Code: String, args: Map<String, String>): String? {
        if (i18Code.isBlank()) {
            return null
        }

        val template = messageSource.getMessage(i18Code, null, LocaleContextHolder.getLocale())

        if (args.isEmpty()) {
            return template
        }

        return StringSubstitutor.replace(template, args, "{", "}")
    }

}
