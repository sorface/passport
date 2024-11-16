package by.sorface.passport.web.dao.models

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@Entity
@Table(name = "T_ROLESTORE")
class RoleEntity : BaseEntity() {
    @Column(name = "C_VALUE")
    var value: String? = null

    @ManyToMany(fetch = FetchType.LAZY)
    var users: List<UserEntity>? = null
}
