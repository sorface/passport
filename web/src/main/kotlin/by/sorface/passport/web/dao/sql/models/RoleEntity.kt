package by.sorface.passport.web.dao.sql.models

import jakarta.persistence.*

@Entity
@Table(name = "T_ROLESTORE")
class RoleEntity : BaseEntity() {
    @Column(name = "C_VALUE")
    var value: String? = null

    @ManyToMany(fetch = FetchType.LAZY)
    var users: List<UserEntity>? = null
}
