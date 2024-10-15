package by.sorface.passport.web.utils.json.mask;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public
enum MaskerFields implements Masker {

    PASSWORDS(Set.of("password", "pass", "pwd")) {
        /**
         * The mask function takes a string and returns a masked version of the string.
         * The mask function will return null if the input is null, otherwise it will
         * return a new String with all characters replaced by '*' except for the first
         * 10 characters. If there are less than 10 characters in the input, then only as many stars as there are letters in value will be returned.  For example:&lt;br&gt;
         * &lt;code&gt;mask(&quot;1234567890&quot;)&lt;/code&gt;&lt;br&gt;will return&lt;br&gt;&lt;code&gt;&quot;**********&quot;&lt;/code&gt;.&lt;br&gt;and:&lt;br&gt;&lt;code&gt;mask(&quot;
         *
         * @param value Mask the value
         * @return A string that is the same length as the input value, but with all characters replaced by asterisks
         */
        @Override
        public String mask(final String value) {
            if (Objects.isNull(value)) {
                return null;
            }

            return value.replaceAll(".", "*").substring(0, Math.min(10, value.length()));
        }

    },

    EMAILS(Set.of("email", "mail")) {
        /**
         * The mask function takes a String and returns a masked version of the string.
         * The mask function will replace all characters in the string except for the first three and last four with an asterisk (*).
         * If there are less than seven characters, then all but the first character will be replaced with an asterisk (*).
         *
         * @param value Mask the email address
         * @return A string with the first three characters
         */
        @Override
        public String mask(final String value) {
            if (Objects.isNull(value)) {
                return null;
            }

            return value.replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*");
        }
    },

    TOKEN(Set.of("token", "hash")) {
        /**
         * The mask function takes a string and returns a new string with every other character replaced by an asterisk.
         *
         * @param value Mask the value
         *
         * @return A string that is the same length as the input,
         */
        @Override
        public String mask(final String value) {
            if (Objects.isNull(value)) {
                return null;
            }

            final char[] tmp = new char[value.length()];
            for (int i = 0; i < value.length(); i++) {
                tmp[i] = (i % 2 == 0) ? value.charAt(i) : '*';
            }

            return new String(tmp);
        }
    },

    UNKNOWN(Set.of()) {
        /**
         * The mask function is used to mask the value of a field.
         *
         * @param value Pass in the value of the field
         *
         * @return A string that is the same length as the input value
         */
        @Override
        public String mask(final String value) {
            return value;
        }

    };

    private final Set<String> fieldNames;

    /**
     * The findByFieldName function takes a String fieldName as an argument and returns the corresponding MaskerFields enum value.
     * If no match is found, it returns UNKNOWN.
     *
     * @param fieldName Find the corresponding maskerfields enum value
     * @return The maskerfields enum value that matches the fieldname parameter
     */
    public static MaskerFields findByFieldName(final String fieldName) {
        if (Objects.isNull(fieldName)) {
            return UNKNOWN;
        }

        return Arrays.stream(MaskerFields.values())
                .filter(maskerFields -> maskerFields.getFieldNames().stream().anyMatch(field -> field.equalsIgnoreCase(fieldName)))
                .findFirst()
                .orElse(UNKNOWN);
    }

}
