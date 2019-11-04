package it.barusu.paymen.channel

interface Processor {
    companion object {
        const val NAME_SUFFIX: String = "Processor"
    }

    fun <T : Response> execute(request: Request): T
    fun <T : Response> handle(content: String, request: Request): T
}
