package by.sorface.passport.web.constants

import lombok.Getter
import lombok.RequiredArgsConstructor
import java.util.*
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.stream.Collectors

@RequiredArgsConstructor
@Getter
enum class OAuthProvider(val value: String) {
    GOOGLE("google"),

    GITHUB("github"),

    YANDEX("yandex"),

    TWITCH("twitch"),

    UNKNOWN("unknown");

    companion object {
        private val PROVIDER_MAP: Map<String, OAuthProvider> = entries.stream()
            .collect(
                Collectors.toMap(
                    { it: OAuthProvider -> it.value.lowercase(Locale.getDefault()) },
                    Function.identity(),
                    { firstMerge: OAuthProvider, _: OAuthProvider? -> firstMerge })
            )

        fun findByName(name: String): OAuthProvider {
            if (Objects.isNull(name)) {
                return UNKNOWN
            }

            return PROVIDER_MAP.getOrDefault(name.lowercase(Locale.getDefault()), UNKNOWN)
        }
    }
}
