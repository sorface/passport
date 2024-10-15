package by.sorface.passport.web.dao.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "T_ROLESTORE")
public class RoleEntity extends BaseEntity {

    @Column(name = "C_VALUE")
    private String value;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<UserEntity> users;

}
