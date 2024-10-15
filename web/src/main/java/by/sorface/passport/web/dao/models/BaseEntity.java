package by.sorface.passport.web.dao.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "C_ID")
    private UUID id;

    @CreatedDate
    @Column(name = "C_CREATEDDDATE")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "C_MODIFIEDDATE")
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @CreatedBy
    @JoinColumn(name = "C_CREATEDBY")
    @ToString.Exclude
    private UserEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @LastModifiedBy
    @JoinColumn(name = "C_MODIFIEDBY")
    @ToString.Exclude
    private UserEntity modifiedBy;

}
