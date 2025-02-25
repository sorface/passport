package by.devpav.kotlin.oidcidp.utils

import java.util.*

object UserUtils {

    fun parseFullName(name: String): DefaultFullName {
        val defaultFullName = DefaultFullName()

        val strings = Arrays.stream(name.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            .map { obj: String -> obj.trim { it <= ' ' } }
            .filter { part: String -> part.isNotEmpty() }
            .toArray()

        if (strings.size > 1) {
            defaultFullName.firstName = strings[0].toString()
        }

        if (strings.size >= 2) {
            defaultFullName.lastName = strings[1].toString()
        }

        if (strings.size >= 3) {
            defaultFullName.otherName = strings[2].toString()
        }

        return defaultFullName
    }

    class DefaultFullName {
        var firstName: String? = null

        var lastName: String? = null

        var otherName: String? = null
    }
}
