package by.devpav.kotlin.oidcidp.web.rest.model.apps

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

/**
 * Класс, представляющий приложение-клиент.
 *
 * @property id уникальный идентификатор приложения
 * @property clientId идентификатор клиента
 * @property clientSecret секрет клиента
 * @property clientName имя клиента
 * @property issueTime время выдачи токена
 * @property expiresAt время истечения токена
 * @property redirectUrls список URL для перенаправления
 * @property postLogoutUrl URL для перенаправления после выхода из системы
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class ApplicationClient {
    var id: String? = null

    var clientId: String? = null

    var clientSecret: String? = null

    var clientName: String? = null

    var issueTime: LocalDateTime? = null

    var expiresAt: LocalDateTime? = null

    var redirectUrls: Set<String>? = null

    var postLogoutUrl: String? = null
}
