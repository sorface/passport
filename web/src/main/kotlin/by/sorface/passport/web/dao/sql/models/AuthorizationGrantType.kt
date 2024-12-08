package by.sorface.passport.web.dao.sql.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "T_AUTHGRANTTYPE")
class AuthorizationGrantType : BaseEntity() {
    @Column(name = "C_NAME")
    var name: String? = null
}
