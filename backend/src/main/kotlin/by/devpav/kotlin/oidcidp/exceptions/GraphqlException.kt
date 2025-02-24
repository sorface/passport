package by.devpav.kotlin.oidcidp.exceptions

open class GraphqlException(val i18Code: String, val args: Map<String, Any> = mapOf()) : RuntimeException(i18Code) {

    constructor(i18Code: String, originMessage: String) : this(i18Code)

}

class GraphqlUserException(i18Code: String, args: Map<String, Any> = mapOf()) : GraphqlException(i18Code)

class GraphqlServerException(i18Code: String, args: Map<String, Any> = mapOf()) : GraphqlException(i18Code)