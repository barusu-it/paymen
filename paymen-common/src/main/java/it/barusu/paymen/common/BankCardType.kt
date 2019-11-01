package it.barusu.paymen.common

enum class BankCardType(val description: String) {
    DEBIT_CARD("借记卡"),
    CREDIT_CARD("贷记卡"),
    QUAST_CREDIT_CARD("准贷记卡"),
    PREPAID_CARD("预付卡"),
    ;
}