package by.sorface.passport.web.security.constants

enum class WhitelistUrls(val patterns: List<String>) {
    OPTION_REQUEST(listOf("/**")),

    API_ACCOUNT(listOf("/api/accounts/current")),
    SIGN_UP(listOf("/api/accounts/signup")),

    WHITE_LIST(
        listOf(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/health/liveness",
            "/actuator/health/readiness"
        )
    ),

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
        fun toArray(vararg urlPatterns: WhitelistUrls): Array<String> {
            return urlPatterns.flatMap { obj: WhitelistUrls -> obj.patterns }.toTypedArray()
        }
    }
}
