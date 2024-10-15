package by.sorface.passport.web.exceptions;

import java.util.Map;

public interface SystemException {

    /**
     * Get args for code
     *
     * @return args message
     */
    Map<String, String> getArgs();

    /**
     * Exception code
     *
     * @return i18 code
     */
    String getI18Code();

}
