package by.devpav.kotlin.oidcidp.dao.sql.model.client

import by.devpav.kotlin.oidcidp.dao.sql.model.BaseModel
import jakarta.persistence.*

@Entity
@Table(name = "T_CLIENTREDIRECTURLSTORE")
class ClientRedirectUrlModel : BaseModel() {

    @Column(name = "C_URL", nullable = false, unique = true)
    var url: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_FK_REGISTEREDCLIENT")
    var registeredClient: by.devpav.kotlin.oidcidp.dao.sql.model.client.RegisteredClientModel? = null

}