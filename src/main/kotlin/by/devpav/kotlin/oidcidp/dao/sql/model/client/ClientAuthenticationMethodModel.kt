package by.devpav.kotlin.oidcidp.dao.sql.model.client

import by.devpav.kotlin.oidcidp.dao.sql.model.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "T_CLIENTAUTHENTICATIONMETHODSTORE")
class ClientAuthenticationMethodModel : BaseModel() {

    @Column(name = "C_METHOD", unique = true, nullable = false)
    var method: String? = null

}