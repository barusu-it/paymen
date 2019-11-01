package it.barusu.paymen.channel

import it.barusu.paymen.channel.config.ChannelSecret
import it.barusu.paymen.common.Phase
import it.barusu.paymen.common.RequestType
import it.barusu.paymen.common.TransactionType
import java.time.LocalDateTime

abstract class Request(val type: RequestType) {
    val createdTime: LocalDateTime = LocalDateTime.now()
    lateinit var content: String
    lateinit var tranasactionType: TransactionType
    lateinit var phase: Phase
    lateinit var secret: ChannelSecret
}
