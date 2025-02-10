package by.devpav.kotlin.oidcidp.dao.sql.model.client

import by.devpav.kotlin.oidcidp.dao.sql.model.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "T_CLIENTSETTINGSTORE")
class ClientSettingModel : BaseModel() {

    @Column(name = "C_REQUIRECONCEPT")
    var requireConcept: Boolean = false

    @Column(name = "C_REQUIREPROOFKEY")
    var requireProofKey: Boolean = false

}