package by.devpav.kotlin.oidcidp.dao.sql.model.client

import by.devpav.kotlin.oidcidp.dao.sql.model.BaseModel
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "T_REGISTEREDCLIENT")
class RegisteredClientModel : BaseModel() {

    @Column(name = "C_CLIENTID", nullable = false)
    var clientId: String? = null

    @Column(name = "C_CLIENTSECRET")
    var clientSecret: String? = null

    @Column(name = "C_CLIENTNAME", nullable = false)
    var clientName: String? = null

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER, targetEntity = ClientRedirectUrlModel::class)
    @JoinColumn(name = "C_FK_REGISTEREDCLIENT")
    var redirectUris: Set<ClientRedirectUrlModel> = setOf()

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = ClientAuthenticationMethodModel::class)
    @JoinTable(
        name = "LT_CLIENTAUTHENTICATIONMETHODSTORE",
        joinColumns = [JoinColumn(name = "C_FK_REGISTEREDCLIENT")],
        inverseJoinColumns = [
            JoinColumn(name = "C_FK_CLIENTAUTHENTICATIONMETHOD")
        ]
    )
    var methods: Set<ClientAuthenticationMethodModel> = setOf()

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = ClientAuthenticationGrantTypeModel::class)
    @JoinTable(
        name = "LT_CLIENTGRANTTYPESTORE",
        joinColumns = [JoinColumn(name = "C_FK_REGISTEREDCLIENT")],
        inverseJoinColumns = [
            JoinColumn(name = "C_FK_CLIENTAUTHENTICATIONGRANTTYPE")
        ]
    )
    var grantTypes: Set<ClientAuthenticationGrantTypeModel> = setOf()

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = ClientScopeModel::class)
    @JoinTable(
        name = "LT_CLIENTSCOPESTORE",
        joinColumns = [JoinColumn(name = "C_FK_REGISTEREDCLIENT")],
        inverseJoinColumns = [
            JoinColumn(name = "C_FK_CLIENTCLIENTSCOPE")
        ]
    )
    var scopes: Set<ClientScopeModel> = setOf()

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "C_ID", referencedColumnName = "C_ID")
    var clientSetting: ClientSettingModel? = null

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "C_ID", referencedColumnName = "C_ID")
    var tokenSetting: ClientTokenSettingModel? = null

    @Column(name = "C_CLIENTIDISSUEDAT", nullable = false)
    var clientIdIssueAt: Instant? = null

    @Column(name = "C_CLIENTSECRETEXPIRESAT")
    var clientSecretExpiresAt: Instant? = null

}