package it.barusu.paymen.common

enum class TransactionType(val description: String) {
    WITHHOLD("代扣"),
    PAYMENT("代付"),
    RECHARGE("充值"),
    WITHDRAW("提现"),
    TRANSFER("转账"),
    REFUND("退款"),
    ;
}