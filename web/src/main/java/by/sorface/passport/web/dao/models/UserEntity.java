package by.sorface.passport.web.dao.models;

import by.sorface.passport.web.dao.models.enums.ProviderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "T_USERSTORE")
public class UserEntity extends BaseEntity {

    @Column(name = "C_USERNAME", unique = true)
    private String username;

    @Column(name = "C_EMAIL", unique = true)
    private String email;

    @Column(name = "C_PASSWORD")
    private String password;

    @Column(name = "C_AVATARURL")
    private String avatarUrl;

    @Temporal(TemporalType.DATE)
    @Column(name = "C_BIRTHDAY")
    private LocalDate birthday;

    @Column(name = "C_FIRSTNAME")
    private String firstName;

    @Column(name = "C_LASTNAME")
    private String lastName;

    @Column(name = "C_MIDDLENAME")
    private String middleName;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "LT_USER_ROLE_STORE",
            joinColumns = @JoinColumn(name = "C_FK_USER"),
            inverseJoinColumns = @JoinColumn(name = "C_FK_ROLE")
    )
    private List<RoleEntity> roles;

    @Column(name = "C_ISENABLED", nullable = false)
    private boolean enabled;

    @Column(name = "C_CONFIRMED", nullable = false)
    private boolean confirm;

    @Column(name = "C_PROVIDER_ID")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "C_EXTERNAL_ID")
    private String externalId;

}
