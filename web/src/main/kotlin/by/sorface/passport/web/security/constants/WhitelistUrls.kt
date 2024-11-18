package by.sorface.passport.web.security.constants

enum class WhitelistUrls(val patterns: List<String>) {
    OPTION_REQUEST(listOf("/**")),

    API_ACCOUNT(listOf("/api/accounts/current")),

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
