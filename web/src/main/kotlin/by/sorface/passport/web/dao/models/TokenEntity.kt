package by.sorface.passport.web.dao.models

import by.sorface.passport.web.dao.models.enums.TokenOperationType
import jakarta.persistence.*
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@Entity
@Table(name = "T_REGISTRYTOKEN")
class TokenEntity : BaseEntity() {
    @Column(name = "C_HASH")
    var hash: String? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_USERID", referencedColumnName = "C_ID")
    var user: UserEntity? = null

    @Column(name = "C_OPERATION_TYPE")
    @Enumerated(EnumType.STRING)
    var operationType: TokenOperationType? = null
}
