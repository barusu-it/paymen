package it.barusu.paymen.channel

import it.barusu.paymen.util.ApiException

interface Processor {
    companion object {
        const val NAME_SUFFIX: String = "Processor"
    }

    fun <T : Response> execute(request: Request): T {
        throw ApiException(msg = "Request execution is not expected by this processor.")
    }

    fun <T : Response> handle(content: String, request: Request): T  {
        throw ApiException(msg = "Notification is not expected by this processor.")
    }
}
