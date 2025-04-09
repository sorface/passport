package by.sorface.idp.config.security.backchannel.dispatcher

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient

/**
 * Класс определяет событие обратного канала для выхода из системы.
 *
 * @property jwt объект Jwt, содержащий информацию о выходе из системы
 * @property registeredClient идентификатор клиента, для которого происходит выход из системы.
 */
data class BackchannelLogoutEvent(val jwt: Jwt, val registeredClient: RegisteredClient)