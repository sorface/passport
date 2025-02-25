package by.devpav.kotlin.oidcidp.utils

import org.apache.commons.lang3.RandomStringUtils
import java.util.stream.Collectors


object PasswordGenerator {

    fun generateCommonLangPassword(): String {
        val upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true)

        val lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true)

        val numbers = RandomStringUtils.secure().nextNumeric(2)

        val specialChar = RandomStringUtils.random(2, 33, 47, false, false)

        val totalChars = RandomStringUtils.secure().nextAscii(2)

        val combinedChars = upperCaseLetters + lowerCaseLetters + numbers + specialChar + totalChars

        val pwdChars = combinedChars.chars()
            .mapToObj { c: Int -> c.toChar() }
            .collect(Collectors.toList())

        pwdChars.shuffle()

        return pwdChars.joinToString(separator = "")
    }

}