package by.sorface.passport.web.utils.json

import by.sorface.passport.web.utils.json.mask.MaskerFields
import by.sorface.passport.web.utils.json.mask.MaskerFields.Companion.findByFieldName
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.*
import java.util.function.Supplier

object Json {

    private val OBJECT_MAPPER = ObjectMapper()

    fun stringify(element: Any?): String {
        return try {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(element)
        } catch (e: JsonProcessingException) {
            "null"
        }
    }

    fun stringifyWithMasking(element: Any): String? {
        return maskJson(element)
    }

    fun lazyStringify(element: Any): LazyObjectSerializable {
        return LazyObjectSerializable { stringify(element) }
    }

    fun lazyStringifyWithMasking(element: Any): LazyObjectSerializable {
        return LazyObjectSerializable { System.lineSeparator() + stringifyWithMasking(element) }
    }

    private fun maskJson(element: Any): String? {
        try {
            val json = stringify(element)

            val root = ObjectMapper()
                .readTree(json)

            val jsonNodeStack = Stack<JsonNode>()
            run {
                jsonNodeStack.push(root)
            }

            while (!jsonNodeStack.isEmpty()) {
                val currentRoot = jsonNodeStack.pop()

                val jsonNodeIterator = currentRoot.fields()

                while (jsonNodeIterator.hasNext()) {
                    val jsonNodeEntry = jsonNodeIterator.next()

                    val fieldName = jsonNodeEntry.key
                    val fieldValue = jsonNodeEntry.value

                    if (fieldValue is ObjectNode) {
                        jsonNodeStack.push(fieldValue)
                        continue
                    }

                    if (fieldValue is ArrayNode) {
                        for (i in 0 until fieldValue.size()) {
                            val arrayElement = fieldValue[i]
                            jsonNodeStack.push(arrayElement)
                        }
                        continue
                    }

                    val maskerFields = findByFieldName(fieldName)

                    if (MaskerFields.UNKNOWN == maskerFields) {
                        continue
                    }

                    if (!fieldValue.isTextual) {
                        continue
                    }

                    (currentRoot as ObjectNode).put(fieldName, maskerFields.mask(fieldValue.textValue()))
                }
            }

            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(root)
        } catch (e: JsonProcessingException) {
            return null
        }
    }
}
