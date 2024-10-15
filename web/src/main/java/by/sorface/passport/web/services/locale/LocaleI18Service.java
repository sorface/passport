package by.sorface.passport.web.services.locale;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocaleI18Service implements LocaleService {

    private final ResourceBundleMessageSource messageSource;

    @Override
    public String getI18Message(final String i18Code) {
        return getMessage(i18Code, Map.of());
    }

    public String getI18Message(final String i18Code, final Map<String, String> args) {
        return getMessage(i18Code, args);
    }

    private String getMessage(final String i18Code, final Map<String, String> args) {
        if (Objects.isNull(i18Code)) {
            return null;
        }

        final var currentLocale = LocaleContextHolder.getLocale();

        final String template = messageSource.getMessage(i18Code, null, currentLocale);

        if (Objects.isNull(args) || args.isEmpty()) {
            return template;
        }

        return StringSubstitutor.replace(template, args, "{", "}");
    }
}
