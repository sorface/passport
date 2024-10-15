package by.sorface.passport.web.utils.json;

import by.sorface.passport.web.utils.json.mask.MaskerFields;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.UtilityClass;

import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

@UtilityClass
public class Json {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * The stringify function takes an object and returns a string representation of that object.
     *
     * @param object Pass in the object that you want to convert into a json string
     * @return A json string representation of the object
     */
    public String stringify(final Object object) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * The stringifyWithMasking function takes in an object and returns a stringified version of the object.
     * The function also masks any sensitive information that is contained within the object.
     *
     * @param object Pass in the object that is to be masked
     * @return A string
     */
    public String stringifyWithMasking(final Object object) {
        return maskJson(object);
    }

    /**
     * The lazyStringify function takes an object and returns a LazyObjectSerializable.
     * The LazyObjectSerializable is a wrapper around the stringified JSON representation of the object.
     * The serialization is deferred until it's needed, which means that if you never use the value, then it will never be computed.
     *
     * @param object Pass in the object to be serialized
     * @return A lazyobjectserializable
     */
    public LazyObjectSerializable lazyStringify(final Object object) {
        return new LazyObjectSerializable(() -> Json.stringify(object));
    }

    /**
     * The lazyStringifyWithMasking function is a wrapper for the Json.stringifyWithMasking function,
     * which returns a LazyObjectSerializable object that will only be serialized when it is accessed.
     * This allows us to avoid unnecessary serialization of objects that are not used in the final result.
     *
     * @param object Pass in the object that you want to serialize
     * @return A lazyobjectserializable object
     */
    public LazyObjectSerializable lazyStringifyWithMasking(final Object object) {
        return new LazyObjectSerializable(() -> System.lineSeparator() + Json.stringifyWithMasking(object));
    }

    /**
     * The maskJson function takes a JSON string and returns a masked version of the same JSON.
     * The masking is done by replacing all values in fields that are marked as sensitive with asterisks.
     * For example, if the input is:
     * { &quot;name&quot;: &quot;John Doe&quot;, &quot;password&quot;: &quot;secret&quot; }
     * then the output will be:
     * { &quot;name&quot;:     ******,   // name field was not masked because it's not sensitive data.  It's just an example! :)  But you get the idea... :)    &lt;-- I'm smiling at you through my code comments! :D
     *
     * @param object Pass in the object to be masked
     * @return A json string with masked values
     */
    private String maskJson(final Object object) {
        try {
            final String json = stringify(object);

            final JsonNode root = new ObjectMapper()
                    .readTree(json);

            final var jsonNodeStack = new Stack<JsonNode>();
            {
                jsonNodeStack.push(root);
            }

            while (!jsonNodeStack.isEmpty()) {
                final JsonNode currentRoot = jsonNodeStack.pop();

                final Iterator<Map.Entry<String, JsonNode>> jsonNodeIterator = currentRoot.fields();

                while (jsonNodeIterator.hasNext()) {
                    final Map.Entry<String, JsonNode> jsonNodeEntry = jsonNodeIterator.next();

                    final String fieldName = jsonNodeEntry.getKey();
                    final JsonNode fieldValue = jsonNodeEntry.getValue();

                    if (fieldValue instanceof ObjectNode) {
                        jsonNodeStack.push(fieldValue);
                        continue;
                    }

                    if (fieldValue instanceof ArrayNode array) {
                        for (int i = 0; i < array.size(); i++) {
                            final JsonNode arrayElement = array.get(i);
                            jsonNodeStack.push(arrayElement);
                        }
                        continue;
                    }

                    final MaskerFields maskerFields = MaskerFields.findByFieldName(fieldName);

                    if (MaskerFields.UNKNOWN.equals(maskerFields)) {
                        continue;
                    }

                    if (!fieldValue.isTextual()) {
                        continue;
                    }

                    ((ObjectNode) currentRoot).put(fieldName, maskerFields.mask(fieldValue.textValue()));
                }
            }

            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
