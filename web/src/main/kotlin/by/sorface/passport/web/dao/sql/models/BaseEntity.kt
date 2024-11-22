package by.sorface.passport.web.dao.sql.models

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
abstract class BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "C_ID")
    lateinit var id: UUID

    @CreatedDate
    @Column(name = "C_CREATEDDDATE")
    var createdDate: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "C_MODIFIEDDATE")
    var modifiedDate: LocalDateTime? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @CreatedBy
    @JoinColumn(name = "C_CREATEDBY")
    var createdBy: UserEntity? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @LastModifiedBy
    @JoinColumn(name = "C_MODIFIEDBY")
    var modifiedBy: UserEntity? = null

}
