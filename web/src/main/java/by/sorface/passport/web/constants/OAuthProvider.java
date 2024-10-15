package by.sorface.passport.web.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum OAuthProvider {

    GOOGLE("google"),

    GITHUB("github"),

    YANDEX("yandex"),

    TWITCH("twitch"),

    UNKNOWN("unknown");

    private static final Map<String, OAuthProvider> PROVIDER_MAP = Arrays.stream(OAuthProvider.values())
            .collect(Collectors.toMap(it -> it.getName().toLowerCase(), Function.identity(), (firstMerge, secondMerge) -> firstMerge));
    private final String name;

    /**
     * The findByName function takes a String as an argument and returns the corresponding OAuthProvider.
     * If no match is found, it returns UNKNOWN.
     *
     * @param name Find the oauthprovider in the provider_map
     * @return An oauthprovider object
     */
    public static OAuthProvider findByName(final String name) {
        if (Objects.isNull(name)) {
            return UNKNOWN;
        }

        return PROVIDER_MAP.getOrDefault(name.toLowerCase(), UNKNOWN);
    }

}
