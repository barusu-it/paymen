package it.barusu.paymen.channel

import java.time.LocalDateTime

abstract class Response {
    val createdTime: LocalDateTime = LocalDateTime.now()
    var code: String? = null
    var message: String? = null
    var content: String? = null
}