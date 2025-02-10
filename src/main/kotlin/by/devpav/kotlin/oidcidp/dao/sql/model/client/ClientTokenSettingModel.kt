package by.devpav.kotlin.oidcidp.dao.sql.model.client

import by.devpav.kotlin.oidcidp.dao.sql.model.BaseModel
import jakarta.persistence.*
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat

@Entity
@Table(name = "T_CLIENTTOKENSETTINGSTORE")
class ClientTokenSettingModel : BaseModel() {

    @Column(name = "C_ACCESSTOKENFORMAT", nullable = false)
    var accessTokenFormat: String = OAuth2TokenFormat.SELF_CONTAINED.value

    @Enumerated(EnumType.STRING)
    @Column(name = "C_IDTOKENSIGNATUREALGORITHM", nullable = false)
    var idTokenSignatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.ES256

    @Column(name = "C_ACCESSTOKENTIMETOLIVE", nullable = false)
    var accessTokenTimeToLive: Long = 360

    @Column(name = "C_REFRESHTOKENTIMETOLIVE", nullable = false)
    var refreshTokenTimeToLive: Long = 720

    @Column(name = "C_REUSEREFRESHTOKENS", nullable = false)
    var reuseRefreshTokens: Boolean = true

}