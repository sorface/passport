@file:Suppress("DEPRECATION")

package by.sorface.idp.utils

import java.util.*


object PasswordGenerator {

    private const val SYMBOLS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz$-*"

    fun generateCommonLangPassword(): String {

        val rnd = Random()

        val password: StringBuilder = StringBuilder(SYMBOLS.length)

        for (i in SYMBOLS.indices) {
            password.append(SYMBOLS[rnd.nextInt(SYMBOLS.length)])
        }

        return password.toString()
    }

}