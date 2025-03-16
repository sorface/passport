package by.sorface.idp.dao.sql.model.client

import by.sorface.idp.dao.sql.model.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * Класс модели настроек клиента.
 * Этот класс представляет таблицу "T_CLIENTSETTINGSTORE" в базе данных.
 */
@Entity
@Table(name = "T_CLIENTSETTINGSTORE")
class ClientSettingModel : BaseModel() {

    /**
     * Требуется концепция.
     * Это поле указывает, требуется ли концепция для клиента.
     */
    @Column(name = "C_REQUIRECONCEPT")
    var requireConcept: Boolean = false

    /**
     * Требуется ключ доказательства.
     * Это поле указывает, требуется ли ключ доказательства для клиента.
     */
    @Column(name = "C_REQUIREPROOFKEY")
    var requireProofKey: Boolean = false

}