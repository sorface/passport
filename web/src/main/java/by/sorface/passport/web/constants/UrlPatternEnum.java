package by.sorface.passport.web.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum UrlPatternEnum {

    OPTION_REQUEST(List.of("/**")),

    API_ACCOUNT(List.of("/api/accounts/current")),

    PERMIT_ALL_PATTERNS(List.of(
            "/error**",
            "/static/**",
            "/account/**"
    )),

    CSRF(List.of("/api/csrf")),
    ;

    private final List<String> patterns;

    /**
     * The toArray function takes in a variable number of UrlPatternEnum objects and returns an array of Strings.
     * The function first converts the input into a stream, then maps each element to its patterns field,
     * which is itself a collection. Then it flattens the resulting stream by converting it to list and finally returning an array from that list.
     *
     * @param urlPatterns Pass in a variable number of arguments
     * @return An array of strings
     */
    public static String[] toArray(UrlPatternEnum... urlPatterns) {
        return Arrays.stream(urlPatterns).map(UrlPatternEnum::getPatterns).flatMap(Collection::stream).toList().toArray(String[]::new);
    }
}
