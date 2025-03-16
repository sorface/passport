package by.sorface.idp.web.rest.model.errors

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Класс, представляющий ошибку REST API
 *
 * @property message Сообщение об ошибке.
 * @property code Код ошибки.
 * @property spanId Идентификатор span (необязательный).
 * @property traceId Идентификатор trace (необязательный).
 */
data class RestError(
    val message: String,
    val code: Int,
    @JsonIgnore
    var spanId: String? = null,
    @JsonIgnore
    var traceId: String? = null
)