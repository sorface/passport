package by.sorface.passport.web.services.locale;

import java.util.Map;

/**
 * Interface for LocaleService.
 * This interface provides methods for managing internationalization (i18n) messages.
 */
public interface LocaleService {

    /**
     * Gets an internationalization (i18n) message by its i18n code.
     *
     * @param i18Code The i18n code of the message to be retrieved.
     * @return The retrieved i18n message.
     */
    String getI18Message(final String i18Code);

    /**
     * Gets an internationalization (i18n) message by its i18n code and arguments.
     * @param i18Code The i18n code of the message to be retrieved.
     * @param args The arguments to be used in the message.
     * @return The retrieved i18n message.
     */
    String getI18Message(final String i18Code, final Map<String, String> args);

}

