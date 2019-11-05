package it.barusu.paymen.channel.config

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChannelRepository : JpaRepository<Channel, Long> {
    fun findByChannelNo(channelNo: String): Optional<Channel>
}