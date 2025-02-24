package by.devpav.kotlin.oidcidp.config.security.constants

/**
 * Класс SessionAttributes содержит константы, используемые для хранения атрибутов сессии.
 */
object SessionAttributes {

    /**
     * Константа SAVED_REQUEST содержит имя атрибута, используемого для сохранения запроса.
     */
    const val SAVED_REQUEST: String = "SPRING_SECURITY_SAVED_REQUEST"

    /**
     * Константа AUTHENTICATION_EXCEPTION содержит имя атрибута, используемого для хранения исключения аутентификации.
     */
    const val AUTHENTICATION_EXCEPTION: String = "SPRING_SECURITY_LAST_EXCEPTION"

    /**
     * Константа USER_AGENT содержит имя атрибута, используемого для хранения информации о пользовательском агенте.
     */
    const val USER_AGENT: String = "USER_AGENT"

}
