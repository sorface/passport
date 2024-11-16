package by.sorface.passport.web.constants

import lombok.Getter
import lombok.RequiredArgsConstructor
import java.util.*
import java.util.function.Function

@Getter
@RequiredArgsConstructor
enum class UrlPatternEnum(private val patterns: List<String>) {
    OPTION_REQUEST(listOf<String>("/**")),

    API_ACCOUNT(listOf<String>("/api/accounts/current")),

    PERMIT_ALL_PATTERNS(
        listOf<String>(
            "/error**",
            "/static/**",
            "/account/**"
        )
    ),

    CSRF(listOf<String>("/api/csrf")),
    ;

    companion object {
        fun toArray(vararg urlPatterns: UrlPatternEnum): Array<String> {
            return urlPatterns.flatMap { obj: UrlPatternEnum -> obj.patterns }.toTypedArray()
        }
    }
}
