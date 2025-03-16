package by.sorface.idp.dao.sql.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "T_ROLESTORE")
class RoleModel : BaseModel() {

    @Column(name = "C_VALUE")
    var value: String? = null

}
