package by.sorface.passport.web.dao.models;

import by.sorface.passport.web.dao.models.enums.TokenOperationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "T_REGISTRYTOKEN")
public class TokenEntity extends BaseEntity {

    @Column(name = "C_HASH")
    private String hash;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_USERID", referencedColumnName = "C_ID")
    private UserEntity user;

    @Column(name = "C_OPERATION_TYPE")
    @Enumerated(EnumType.STRING)
    private TokenOperationType operationType;

}
