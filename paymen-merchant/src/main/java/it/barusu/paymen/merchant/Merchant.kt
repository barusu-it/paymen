package it.barusu.paymen.merchant

import it.barusu.paymen.common.Status
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "pm_merchant", uniqueConstraints = [UniqueConstraint(columnNames = ["merchant_no"])])
data class Merchant(
        @Id
        @GeneratedValue
        var id: Long? = null,

        @Version
        var version: Long? = null,

        @Column(length = 64, nullable = false)
        var merchantNo: String? = null,

        @Column(nullable = false)
        var name: String? = null,

        @Column
        var description: String? = null,

        @CreatedDate
        @Column(nullable = false)
        var createdTime: LocalDateTime? = null,

        @LastModifiedDate
        @Column(nullable = false)
        var updatedTime: LocalDateTime? = null,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var status: Status? = null
)