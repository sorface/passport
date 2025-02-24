package by.devpav.kotlin.oidcidp.dao.sql.model.client

import by.devpav.kotlin.oidcidp.dao.sql.model.BaseModel
import jakarta.persistence.*

/**
 * Класс модели перенаправления клиента.
 * Этот класс представляет таблицу "T_CLIENTREDIRECTURLSTORE" в базе данных.
 */
@Entity
@Table(name = "T_CLIENTREDIRECTURLSTORE")
class ClientRedirectUrlModel : BaseModel() {

    /**
     * URL перенаправления клиента.
     * Это поле не может быть null и должно быть уникальным.
     */
    @Column(name = "C_URL", nullable = false, unique = true)
    var url: String? = null

    /**
     * Связь с зарегистрированным клиентом.
     * Это поле представляет отношение "многие к одному" с RegisteredClientModel.
     * Загрузка связанных данных выполняется лениво.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_FK_REGISTEREDCLIENT")
    var registeredClient: RegisteredClientModel? = null

}