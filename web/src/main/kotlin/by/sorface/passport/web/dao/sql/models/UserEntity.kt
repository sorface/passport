package by.sorface.passport.web.dao.sql.models

import by.sorface.passport.web.dao.sql.models.enums.ProviderType
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "T_USERSTORE")
class UserEntity : BaseEntity() {

    @Column(name = "C_USERNAME", unique = true)
    var username: String? = null

    @Column(name = "C_EMAIL", unique = true)
    var email: String? = null

    @Column(name = "C_PASSWORD")
    var password: String? = null

    @Column(name = "C_AVATARURL")
    var avatarUrl: String? = null

    @Temporal(TemporalType.DATE)
    @Column(name = "C_BIRTHDAY")
    var birthday: LocalDate? = null

    @Column(name = "C_FIRSTNAME")
    var firstName: String? = null

    @Column(name = "C_LASTNAME")
    var lastName: String? = null

    @Column(name = "C_MIDDLENAME")
    var middleName: String? = null

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "LT_USER_ROLE_STORE",
        joinColumns = [JoinColumn(name = "C_FK_USER")],
        inverseJoinColumns = [
            JoinColumn(name = "C_FK_ROLE")
        ]
    )
    var roles: List<RoleEntity> = listOf()

    @Column(name = "C_ISENABLED", nullable = false)
    var enabled = true

    @Column(name = "C_CONFIRMED", nullable = false)
    var confirm = false

    @Column(name = "C_PROVIDER_ID")
    @Enumerated(EnumType.STRING)
    var providerType: ProviderType? = null

    @Column(name = "C_EXTERNAL_ID")
    var externalId: String? = null

}
