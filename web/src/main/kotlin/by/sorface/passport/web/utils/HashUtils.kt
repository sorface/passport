package by.sorface.passport.web.utils

import org.apache.commons.lang3.RandomStringUtils

object HashUtils {
    fun generateRegistryHash(length: Int): String {
        return RandomStringUtils.randomAlphabetic(length)
    }
}
