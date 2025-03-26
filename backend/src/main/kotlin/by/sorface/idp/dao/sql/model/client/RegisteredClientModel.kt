package by.sorface.idp.dao.sql.model.client

import by.sorface.idp.dao.sql.model.BaseModel
import jakarta.persistence.*
import java.time.Instant

/**
 * Класс модели зарегистрированного клиента.
 * Этот класс представляет таблицу "T_REGISTEREDCLIENT" в базе данных.
 */
@Entity
@Table(name = "T_REGISTEREDCLIENT")
class RegisteredClientModel : BaseModel() {

    /**
     * Идентификатор клиента.
     * Это поле не может быть null.
     */
    @Column(name = "C_CLIENTID", nullable = false)
    var clientId: String? = null

    /**
     * Секрет клиента.
     */
    @Column(name = "C_CLIENTSECRET")
    var clientSecret: String? = null

    /**
     * Имя клиента.
     * Это поле не может быть null.
     */
    @Column(name = "C_CLIENTNAME", nullable = false)
    var clientName: String? = null

    /**
     * URI перенаправления клиента.
     * Это поле представляет отношение "один ко многим" с ClientRedirectUrlModel.
     * Загрузка связанных данных выполняется немедленно.
     */
    @OneToMany(
        mappedBy = "registeredClient",
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER,
        targetEntity = ClientRedirectUrlModel::class
    )
    var redirectUris: Set<ClientRedirectUrlModel> = setOf()

    /**
     * URI перенаправления клиента.
     * Это поле представляет отношение "один ко многим" с ClientRedirectUrlModel.
     * Загрузка связанных данных выполняется немедленно.
     */
    @OneToMany(
        mappedBy = "registeredClient",
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER,
        targetEntity = PostLogoutRedirectUrlModel::class
    )
    var postLogoutRedirectUrls: Set<PostLogoutRedirectUrlModel> = setOf()

    /**
     * Методы аутентификации клиента.
     * Это поле представляет отношение "многие ко многим" с ClientAuthenticationMethodModel.
     * Загрузка связанных данных выполняется немедленно.
     */
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = ClientAuthenticationMethodModel::class)
    @JoinTable(
        name = "LT_CLIENTAUTHENTICATIONMETHODSTORE",
        joinColumns = [JoinColumn(name = "C_FK_REGISTEREDCLIENT")],
        inverseJoinColumns = [
            JoinColumn(name = "C_FK_CLIENTAUTHENTICATIONMETHOD")
        ]
    )
    var methods: Set<ClientAuthenticationMethodModel> = setOf()

    /**
     * Типы аутентификации клиента.
     * Это поле представляет отношение "многие ко многим" с ClientAuthenticationGrantTypeModel.
     * Загрузка связанных данных выполняется немедленно.
     */
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = ClientAuthenticationGrantTypeModel::class)
    @JoinTable(
        name = "LT_CLIENTGRANTTYPESTORE",
        joinColumns = [JoinColumn(name = "C_FK_REGISTEREDCLIENT")],
        inverseJoinColumns = [
            JoinColumn(name = "C_FK_CLIENTAUTHENTICATIONGRANTTYPE")
        ]
    )
    var grantTypes: Set<ClientAuthenticationGrantTypeModel> = setOf()

    /**
     * Области видимости клиента.
     * Это поле представляет отношение "многие ко многим" с ClientScopeModel.
     * Загрузка связанных данных выполняется немедленно.
     */
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = ClientScopeModel::class)
    @JoinTable(
        name = "LT_CLIENTSCOPESTORE",
        joinColumns = [JoinColumn(name = "C_FK_REGISTEREDCLIENT")],
        inverseJoinColumns = [
            JoinColumn(name = "C_FK_CLIENTCLIENTSCOPE")
        ]
    )
    var scopes: Set<ClientScopeModel> = setOf()

    /**
     * Настройки клиента.
     * Это поле представляет отношение "один к одному" с ClientSettingModel.
     * Загрузка связанных данных выполняется немедленно.
     */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "C_ID", referencedColumnName = "C_ID")
    var clientSetting: ClientSettingModel? = null

    /**
     * Настройки токена клиента.
     * Это поле представляет отношение "один к одному" с ClientTokenSettingModel.
     * Загрузка связанных данных выполняется немедленно.
     */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "C_ID", referencedColumnName = "C_ID")
    var tokenSetting: ClientTokenSettingModel? = null

    /**
     * Дата и время выдачи идентификатора клиента.
     * Это поле не может быть null.
     */
    @Column(name = "C_CLIENTIDISSUEDAT", nullable = false)
    var clientIdIssueAt: Instant? = null

    /**
     * Дата и время истечения срока действия секрета клиента.
     */
    @Column(name = "C_CLIENTSECRETEXPIRESAT")
    var clientSecretExpiresAt: Instant? = null

}