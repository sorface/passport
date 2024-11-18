package by.sorface.passport.web.constants

import by.sorface.passport.web.security.oauth2.provider.enums.OAuth2ProviderType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class OAuth2ProviderTypeTest {

    @ParameterizedTest
    @MethodSource("providerFactory")
    fun findByName(providerName: String?, expectedProvider: OAuth2ProviderType?) {
        val provider: OAuth2ProviderType = OAuth2ProviderType.findByName(providerName)
        Assertions.assertEquals(expectedProvider, provider)
    }

    companion object {

        @JvmStatic
        fun providerFactory(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("GITHUB", OAuth2ProviderType.GITHUB),
                Arguments.of("github", OAuth2ProviderType.GITHUB),
                Arguments.of("GitHub", OAuth2ProviderType.GITHUB),
                Arguments.of("google", OAuth2ProviderType.GOOGLE),
                Arguments.of("GOOGLE", OAuth2ProviderType.GOOGLE),
                Arguments.of("GooGLE", OAuth2ProviderType.GOOGLE),
                Arguments.of("yandex", OAuth2ProviderType.YANDEX),
                Arguments.of("Yandex", OAuth2ProviderType.YANDEX),
                Arguments.of("YANDEX", OAuth2ProviderType.YANDEX),
                Arguments.of("twitch", OAuth2ProviderType.TWITCH),
                Arguments.of("TWITCH", OAuth2ProviderType.TWITCH),
                Arguments.of("Twitch", OAuth2ProviderType.TWITCH),
                Arguments.of("another", OAuth2ProviderType.UNKNOWN),
                Arguments.of("", OAuth2ProviderType.UNKNOWN),
                Arguments.of(null, OAuth2ProviderType.UNKNOWN)
            )
        }

    }

}