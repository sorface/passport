package by.sorface.passport.web.utils;

import lombok.Data;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class UserUtils {

    public DefaultFullName parseFullName(final String name) {
        final var defaultFullName = new DefaultFullName();

        final var strings = Arrays.stream(name.split(" "))
                .map(String::trim)
                .filter(part -> !part.isEmpty())
                .toArray(String[]::new);

        if (strings.length > 1) {
            defaultFullName.setFirstName(strings[0]);
        }

        if (strings.length >= 2) {
            defaultFullName.setLastName(strings[1]);
        }

        if (strings.length >= 3) {
            defaultFullName.setOtherName(strings[2]);
        }

        return defaultFullName;
    }

    @Data
    public static class DefaultFullName {

        private String firstName;

        private String lastName;

        private String otherName;

    }

}
