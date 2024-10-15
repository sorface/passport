package by.sorface.passport.web.constants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class OAuthProviderTest {

    static Stream<Arguments> providerFactory() {
        return Stream.of(
                Arguments.of("GITHUB", OAuthProvider.GITHUB),
                Arguments.of("github", OAuthProvider.GITHUB),
                Arguments.of("GitHub", OAuthProvider.GITHUB),
                Arguments.of("google", OAuthProvider.GOOGLE),
                Arguments.of("GOOGLE", OAuthProvider.GOOGLE),
                Arguments.of("GooGLE", OAuthProvider.GOOGLE),
                Arguments.of("yandex", OAuthProvider.YANDEX),
                Arguments.of("Yandex", OAuthProvider.YANDEX),
                Arguments.of("YANDEX", OAuthProvider.YANDEX),
                Arguments.of("twitch", OAuthProvider.TWITCH),
                Arguments.of("TWITCH", OAuthProvider.TWITCH),
                Arguments.of("Twitch", OAuthProvider.TWITCH),
                Arguments.of("another", OAuthProvider.UNKNOWN),
                Arguments.of("", OAuthProvider.UNKNOWN),
                Arguments.of(null, OAuthProvider.UNKNOWN)
        );
    }

    @ParameterizedTest
    @MethodSource("providerFactory")
    void findByName(final String providerName, final OAuthProvider expectedProvider) {
        final var provider = OAuthProvider.findByName(providerName);
        Assertions.assertEquals(expectedProvider, provider);
    }

}