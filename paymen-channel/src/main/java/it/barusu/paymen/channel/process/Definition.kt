package it.barusu.paymen.channel.process

import it.barusu.paymen.channel.Request
import it.barusu.paymen.channel.Response
import it.barusu.paymen.common.RequestType

data class TransactionRequest(var orderNo: String? = null,
                              var transaction: Transaction? = null)
    : Request(type = RequestType.TRANSACTION)

data class TransactionResponse(var orderNo: String? = null,
                               var transaction: Transaction? = null) : Response()

data class TransactionQueryRequest(var orderNo: String? = null,
                                   var transaction: Transaction? = null)
    : Request(type = RequestType.TRANSACTION_QUERY)

data class TransactionQueryResponse(var orderNo: String? = null,
                                    var transaction: Transaction? = null) : Response()

data class TransactionNotificationRequest(var transactionNo: String? = null,
                                          var transaction: Transaction? = null)
    : Request(type = RequestType.TRANSACTION_QUERY)

data class TransactionNotificationResponse(var transactionNo: String? = null,
                                           var transaction: Transaction? = null) : Response()


