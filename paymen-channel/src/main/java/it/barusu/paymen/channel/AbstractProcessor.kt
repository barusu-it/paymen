package it.barusu.paymen.channel

import it.barusu.paymen.util.ApiException

abstract class AbstractProcessor : Processor {

    override fun <T : Response> execute(request: Request): T {
        throw ApiException(msg = "Request execution is not expected by this processor.")
    }

    override fun <T : Response> handle(content: String, request: Request): T {
        throw ApiException(msg = "Notification is not expected by this processor.")
    }
}