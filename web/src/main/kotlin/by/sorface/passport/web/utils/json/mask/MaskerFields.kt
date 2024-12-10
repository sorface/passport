package by.sorface.passport.web.utils.json.mask

import java.util.*
import kotlin.math.min

enum class MaskerFields(val fieldNames: Set<String>) : Masker {

    PASSWORDS(setOf("password", "pass", "pwd")) {
        override fun mask(value: String?): String? {
            if (Objects.isNull(value)) {
                return null
            }

            return value!!.replace(".".toRegex(), "*").substring(0, min(10.0, value.length.toDouble()).toInt())
        }
    },

    EMAILS(setOf("email", "mail", "to")) {
        override fun mask(value: String?): String? {
            if (Objects.isNull(value)) {
                return null
            }

            return value!!.replace("(^[^@]{3}|(?!^)\\G)[^@]".toRegex(), "$1*")
        }
    },

    TOKEN(setOf("token", "hash")) {
        override fun mask(value: String?): String? {
            if (Objects.isNull(value)) {
                return null
            }

            val tmp = CharArray(value!!.length)
            for (i in value.indices) {
                tmp[i] = if ((i % 2 == 0)) value[i] else '*'
            }

            return String(tmp)
        }
    },

    UNKNOWN(setOf()) {
        override fun mask(value: String?): String? {
            return value
        }
    };

    companion object {
        /**
         * The findByFieldName function takes a String fieldName as an argument and returns the corresponding MaskerFields enum value.
         * If no match is found, it returns UNKNOWN.
         *
         * @param fieldName Find the corresponding maskerfields enum value
         * @return The maskerfields enum value that matches the fieldname parameter
         */
        @JvmStatic
        fun findByFieldName(fieldName: String?): MaskerFields {
            if (Objects.isNull(fieldName)) {
                return UNKNOWN
            }

            return Arrays.stream(entries.toTypedArray())
                .filter { maskerFields: MaskerFields ->
                    maskerFields.fieldNames.stream().anyMatch { field: String -> field.equals(fieldName, ignoreCase = true) }
                }
                .findFirst()
                .orElse(UNKNOWN)
        }
    }
}
