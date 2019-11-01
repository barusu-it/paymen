package it.barusu.paymen.channel

import java.time.LocalDateTime

abstract class Response {
    val createdTime: LocalDateTime = LocalDateTime.now()
    lateinit var code: String
    lateinit var message: String
    lateinit var content: String
}