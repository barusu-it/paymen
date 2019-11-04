package it.barusu.paymen.channel

interface Converter {
    fun writeTo(request: Request): String
    fun readFrom(content: String, request: Request): Response
}