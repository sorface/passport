package by.devpav.kotlin.oidcidp.dao.sql.model.client

import by.devpav.kotlin.oidcidp.dao.sql.model.BaseModel
import jakarta.persistence.*

@Entity
@Table(name = "T_CLIENTAUTHENTICATIONGRANTTYPESTORE")
class ClientAuthenticationGrantTypeModel : BaseModel() {

    @Column(name = "C_GRANTTYPE", unique = true, nullable = false)
    var grantType: String? = null

}