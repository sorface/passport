package by.sorface.idp.dao.sql.model.client

import by.sorface.idp.dao.sql.model.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * Класс модели области клиента.
 * Этот класс представляет таблицу "T_CLIENTAUTHENTICATIONSCOPESTORE" в базе данных.
 */
@Entity
@Table(name = "T_CLIENTAUTHENTICATIONSCOPESTORE")
class ClientScopeModel : BaseModel() {

    /**
     * Область клиента.
     * Это поле должно быть уникальным.
     */
    @Column(name = "C_SCOPE", unique = true)
    var scope: String? = null

}