package by.devpav.kotlin.oidcidp.dao.sql.model.client

import by.devpav.kotlin.oidcidp.dao.sql.model.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "T_CLIENTAUTHENTICATIONSCOPESTORE")
class ClientScopeModel : BaseModel() {

    @Column(name = "C_SCOPE", unique = true)
    var scope: String? = null

}