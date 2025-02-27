package by.devpav.kotlin.oidcidp.web.rest.model.apps

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

/**
 * Класс, представляющий обновление секрета приложения.
 *
 * @property clientSecret новый секрет приложения
 * @property expiresAt время истечения секрета
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class ApplicationRefreshSecret {

    var clientSecret: String? = null

    var expiresAt: LocalDateTime? = null

}