package by.sorface.passport.web.utils.json

import java.util.function.Supplier

class LazyObjectSerializable(private val stringSupplier: Supplier<String>) {
    override fun toString(): String {
        return stringSupplier.get()
    }
}
