package by.sorface.passport.web.security.constants

enum class WhitelistUrls(val patterns: List<String>) {
    OPTION_REQUEST(listOf("/**")),

    API_ACCOUNT(listOf("/api/accounts/current")),

    SIGN_UP(
        listOf(
            "/api/accounts/signup",
            "/api/accounts/confirm/otp"
        )
    ),

    WHITE_LIST(
        listOf(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/health/liveness",
            "/actuator/health/readiness",
            "/api/accounts/confirm",
            "/api/accounts/otp",
            "/api/accounts/*/exists"
        )
    ),

    CSRF(listOf("/api/csrf")),
    ;

    companion object {
        fun toArray(vararg urlPatterns: WhitelistUrls): Array<String> {
            return urlPatterns.flatMap { obj: WhitelistUrls -> obj.patterns }.toTypedArray()
        }
    }
}
