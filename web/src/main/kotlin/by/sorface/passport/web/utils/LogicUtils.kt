package by.sorface.passport.web.utils

object LogicUtils {
    fun not(condition: Boolean): Boolean {
        return !condition
    }

    fun or(vararg conditions: Boolean): Boolean {
        return conditions.any { it }
    }
}
