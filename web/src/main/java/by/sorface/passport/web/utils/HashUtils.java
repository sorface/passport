package by.sorface.passport.web.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class HashUtils {

    /**
     * The generateRegistryHash function generates a random alphabetic string of the specified length.
     *
     * @param length Generate a random string of the length specified
     * @return A random alphabetic string of the specified length
     */
    public String generateRegistryHash(final int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

}
