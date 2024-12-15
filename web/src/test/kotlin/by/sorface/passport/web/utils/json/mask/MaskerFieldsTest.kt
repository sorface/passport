package by.sorface.passport.web.utils.json.mask

import by.sorface.passport.web.utils.json.mask.MaskerFields.Companion.findByFieldName
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class MaskerFieldsTest {
    @ParameterizedTest
    @MethodSource("maskFieldsProviderFactory")
    fun findByFieldName(field: String?, expectedMaskerField: MaskerFields?) {
        val actualField = findByFieldName(field)

        Assertions.assertEquals(expectedMaskerField, actualField)
    }

    companion object {

        @JvmStatic
        fun maskFieldsProviderFactory(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("pwd", MaskerFields.PASSWORDS),
                Arguments.of("password", MaskerFields.PASSWORDS),
                Arguments.of("pass", MaskerFields.PASSWORDS),
                Arguments.of("email", MaskerFields.EMAILS),
                Arguments.of("mail", MaskerFields.EMAILS),
                Arguments.of("token", MaskerFields.TOKEN),
                Arguments.of("hash", MaskerFields.TOKEN),
                Arguments.of(null, MaskerFields.UNKNOWN)
            )
        }
    }

}