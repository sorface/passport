package by.sorface.passport.web.dao.sql.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "T_ROLESTORE")
class RoleEntity : BaseEntity() {
    @Column(name = "C_VALUE")
    var value: String? = null
}