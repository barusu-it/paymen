package it.barusu.paymen.channel.config

import it.barusu.paymen.common.ChannelType
import it.barusu.paymen.common.Status
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "pm_channel")
data class Channel(
        @Id
        @GeneratedValue
        var id: Long? = null,

        @Version
        var version: Long? = null,

        @Column(length = 64, nullable = false)
        var channelNo: String? = null,

        @Enumerated(EnumType.STRING)
        @Column(length = 64, nullable = false)
        var channelType: ChannelType? = null,

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
        @Column(length = 64, nullable = false)
        var status: Status = Status.ACTIVED
)