package by.sorface.idp.dao.sql.model.client

import by.sorface.idp.dao.sql.model.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * Класс ClientAuthenticationMethodModel представляет собой модель для хранения информации о методах аутентификации клиента.
 * Он расширяет класс BaseModel и используется для работы с таблицей "T_CLIENTAUTHENTICATIONMETHODSTORE" в базе данных.
 */
@Entity
@Table(name = "T_CLIENTAUTHENTICATIONMETHODSTORE")
class ClientAuthenticationMethodModel : BaseModel() {

    /**
     * Поле method представляет собой метод аутентификации клиента.
     * Оно хранится в столбце "C_METHOD" таблицы "T_CLIENTAUTHENTICATIONMETHODSTORE".
     * Значение этого поля должно быть уникальным и не может быть null.
     */
    @Column(name = "C_METHOD", unique = true, nullable = false)
    var method: String? = null

}