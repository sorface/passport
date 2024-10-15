package by.sorface.passport.web.dao.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "T_AUTHGRANTTYPE")
public class AuthorizationGrantType extends BaseEntity {

    @Column(name = "C_NAME")
    private String name;

}
