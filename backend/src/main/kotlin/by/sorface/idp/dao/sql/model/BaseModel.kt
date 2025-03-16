package by.sorface.idp.dao.sql.model

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseModel {

    @Id
    @UuidGenerator
    @Column(name = "C_ID")
    var id: UUID? = null

    @CreatedDate
    @Column(name = "C_CREATEDDATE")
    var createdDate: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "C_MODIFIEDDATE")
    var modifiedDate: LocalDateTime? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @CreatedBy
    @JoinColumn(name = "C_CREATEDBY")
    var createdBy: UserModel? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @LastModifiedBy
    @JoinColumn(name = "C_MODIFIEDBY")
    var modifiedBy: UserModel? = null
}
