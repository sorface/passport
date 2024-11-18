package by.sorface.passport.web.dao.sql.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "T_OAUTH2CLIENT")
class OAuth2Client : BaseEntity() {

    @Column(name = "C_CLIENTID", nullable = false)
    var clientId: String? = null

    @Column(name = "C_CLIENTSECRET")
    var clientSecret: String? = null

    @Column(name = "C_CLIENTNAME", nullable = false)
    var clientName: String? = null

    @Column(name = "C_POST_LOGOUT_REDIRECT_URI")
    var postLogoutRedirectUri: String? = null

    @Column(name = "C_REDIRECTURIS")
    var redirectUris: String? = null

    @Column(name = "C_CLIENTIDISSUEDAT", nullable = false)
    var clientIdIssueAt: LocalDateTime? = null

    @Column(name = "C_CLIENTSECRETEXPIRESAT")
    var clientSecretExpiresAt: LocalDateTime? = null

}
