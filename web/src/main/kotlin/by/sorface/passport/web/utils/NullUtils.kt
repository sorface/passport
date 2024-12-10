package by.sorface.passport.web.utils

import java.util.*
import java.util.function.Consumer

object NullUtils {
    fun <T> setIfNonNull(value: T?, consumer: Consumer<T>) = value?.let { consumer.accept(it) }
}
