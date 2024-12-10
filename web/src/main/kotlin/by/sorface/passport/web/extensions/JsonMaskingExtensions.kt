package by.sorface.passport.web.extensions

import by.sorface.passport.web.utils.json.Json
import by.sorface.passport.web.utils.json.LazyObjectSerializable
import by.sorface.passport.web.utils.json.mask.MaskerFields

fun Any?.toJson(mask: Boolean = false): String? {
    this ?: return null
    return if (mask) Json.stringifyWithMasking(this) else Json.stringify(this)
}

fun String?.toStringMask(maskingType: MaskerFields): String? {
    if (this == null) return null

    return maskingType.mask(this)
}

fun Any?.toLazyJson(mask: Boolean = false): LazyObjectSerializable? {
    this ?: return null

    return if (mask) Json.lazyStringifyWithMasking(this) else Json.lazyStringifyWithMasking(this)
}
