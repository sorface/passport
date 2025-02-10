package by.devpav.kotlin.oidcidp.dao.sql.model

import by.devpav.kotlin.oidcidp.dao.sql.model.enums.ProviderType
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "T_USERSTORE")
class UserModel : BaseModel() {

    @Column(name = "C_USERNAME", unique = true, nullable = false)
    var username: String? = null

    @Column(name = "C_EMAIL", unique = true, nullable = false)
    var email: String? = null

    @Column(name = "C_PASSWORD", nullable = false)
    var password: String? = null

    @Column(name = "C_AVATARURL", nullable = true)
    var avatarUrl: String? = null

    @Temporal(TemporalType.DATE)
    @Column(name = "C_BIRTHDAY", nullable = true)
    var birthday: LocalDate? = null

    @Column(name = "C_FIRSTNAME", nullable = true)
    var firstName: String? = null

    @Column(name = "C_LASTNAME", nullable = true)
    var lastName: String? = null

    @Column(name = "C_MIDDLENAME", nullable = true)
    var middleName: String? = null

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "LT_USERROLESTORE",
        joinColumns = [JoinColumn(name = "C_FK_USER")],
        inverseJoinColumns = [
            JoinColumn(name = "C_FK_ROLE")
        ]
    )
    var roles: List<RoleModel> = listOf()

    @Column(name = "C_ISENABLED", nullable = false)
    var enabled = true

    @Column(name = "C_CONFIRMED", nullable = false)
    var confirm = false

    @Column(name = "C_PROVIDER_ID", nullable = true)
    @Enumerated(EnumType.STRING)
    var providerType: ProviderType? = null

    @Column(name = "C_EXTERNAL_ID", nullable = true)
    var externalId: String? = null

}
