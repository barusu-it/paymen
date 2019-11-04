package it.barusu.paymen.channel

import it.barusu.paymen.channel.process.*
import it.barusu.paymen.common.RequestType
import it.barusu.paymen.util.ApiException

abstract class AbstractConverter : Converter {

    override fun writeTo(request: Request): String =
            when (request.type) {
                RequestType.TRANSACTION -> from(request as TransactionRequest)
                RequestType.TRANSACTION_QUERY -> from(request as TransactionQueryRequest)
                RequestType.TRANSACTION_NOTIFICATION -> from(request as TransactionNotificationRequest)
            }

    override fun readFrom(content: String, request: Request): Response =
            when (request.type) {
                RequestType.TRANSACTION -> toTransactionResponse(content, request)
                RequestType.TRANSACTION_QUERY -> toTransactionQueryResponse(content, request)
                RequestType.TRANSACTION_NOTIFICATION -> toTransactionNotificationResponse(content, request)
            }


    open fun from(request: TransactionRequest): String =
            throw ApiException(msg = "Transaction is not supported.")

    open fun from(request: TransactionQueryRequest): String =
            throw ApiException(msg = "Transaction query is not supported.")

    open fun from(request: TransactionNotificationRequest): String =
            throw ApiException(msg = "Transaction notification is not supported.")

    open fun toTransactionResponse(content: String, request: Request): TransactionResponse =
            throw ApiException(msg = "Transaction is not supported.")

    open fun toTransactionQueryResponse(content: String, request: Request): TransactionQueryResponse =
            throw ApiException(msg = "Transaction is not supported.")

    open fun toTransactionNotificationResponse(content: String, request: Request): TransactionNotificationResponse =
            throw ApiException(msg = "Transaction is not supported.")
}