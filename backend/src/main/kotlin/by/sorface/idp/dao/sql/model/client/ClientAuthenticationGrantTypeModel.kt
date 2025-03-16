package by.sorface.idp.dao.sql.model.client

import by.sorface.idp.dao.sql.model.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * Класс ClientAuthenticationGrantTypeModel представляет собой модель для хранения информации о типах аутентификации клиента.
 * Он расширяет класс BaseModel и используется для работы с таблицей "T_CLIENTAUTHENTICATIONGRANTTYPESTORE" в базе данных.
 */
@Entity
@Table(name = "T_CLIENTAUTHENTICATIONGRANTTYPESTORE")
class ClientAuthenticationGrantTypeModel : BaseModel() {

    /**
     * Поле grantType представляет собой тип аутентификации клиента.
     * Оно хранится в столбце "C_GRANTTYPE" таблицы "T_CLIENTAUTHENTICATIONGRANTTYPESTORE".
     * Значение этого поля должно быть уникальным и не может быть null.
     */
    @Column(name = "C_GRANTTYPE", unique = true, nullable = false)
    var grantType: String? = null

}