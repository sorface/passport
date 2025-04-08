package by.sorface.idp.config.security.session

/**
 * Интерфейс для управления сессиями.
 */
interface SessionManager {

    /**
     * Сбрасывает сессию с указанным идентификатором и именем участника.
     *
     * @param sessionId идентификатор сессии.
     * @param principalName имя участника.
     */
    fun resetWithNotify(sessionId: String, principalName: String)

    fun reset(sessionId: String, principalName: String)

}