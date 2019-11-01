package it.barusu.paymen.common

enum class RequestType(val description: String) {
    TRANSACTION("交易"),
    TRANSACTION_QUERY("交易查询"),
    TRANSACTION_NOTIFICATION("交易通知"),
    ;
}