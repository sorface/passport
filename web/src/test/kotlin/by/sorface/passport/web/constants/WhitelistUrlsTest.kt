package by.sorface.passport.web.constants

import by.sorface.passport.web.security.constants.WhitelistUrls
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class WhitelistUrlsTest {
    @Test
    fun toArray() {
        val strings = WhitelistUrls.toArray(WhitelistUrls.CSRF)

        Assertions.assertEquals(1, strings.size)
        Assertions.assertEquals(strings[0], WhitelistUrls.CSRF.patterns.firstOrNull())
    }
}