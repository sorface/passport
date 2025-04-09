package by.sorface.idp.config.security.backchannel.dispatcher

/**
 * Интерфейс представляет собой диспетчер событий для OIDC клиента
 */
interface BackchannelEventDispatcher {

    /**
     * Метод dispatch отправляет событие для OIDC клиента
     */
    fun dispatch(event: BackchannelLogoutEvent)

}

