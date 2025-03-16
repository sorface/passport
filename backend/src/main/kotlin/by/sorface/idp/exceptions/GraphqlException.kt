package by.sorface.idp.exceptions

open class GraphqlException(val i18Code: String, val args: Map<String, Any> = mapOf()) : RuntimeException(i18Code) {

    constructor(i18Code: String, originMessage: String) : this(i18Code)

}

class GraphqlUserException(i18Code: String, args: Map<String, Any> = mapOf()) : GraphqlException(i18Code, args)

class GraphqlServerException(i18Code: String, args: Map<String, Any> = mapOf()) : GraphqlException(i18Code, args)