package by.sorface.passport.web.utils.json.mask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class MaskerFieldsTest {

    static Stream<Arguments> maskFieldsProviderFactory() {
        return Stream.of(
                Arguments.of("pwd", MaskerFields.PASSWORDS),
                Arguments.of("password", MaskerFields.PASSWORDS),
                Arguments.of("pass", MaskerFields.PASSWORDS),
                Arguments.of("email", MaskerFields.EMAILS),
                Arguments.of("mail", MaskerFields.EMAILS),
                Arguments.of("token", MaskerFields.TOKEN),
                Arguments.of("hash", MaskerFields.TOKEN),
                Arguments.of(null, MaskerFields.UNKNOWN)
        );
    }

    @ParameterizedTest
    @MethodSource("maskFieldsProviderFactory")
    void findByFieldName(final String field, final MaskerFields expectedMaskerField) {
        final var actualField = MaskerFields.findByFieldName(field);

        Assertions.assertEquals(expectedMaskerField, actualField);
    }

}