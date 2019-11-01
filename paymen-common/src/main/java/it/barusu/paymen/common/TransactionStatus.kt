package it.barusu.paymen.common

enum class TransactionStatus(val description: String) {
    CREATED("已创建"),
    ORDERED("已下单"),
    SUCCEED("已成功"),
    FAILED("已失败"),
    PROCESSING("处理中"),
    CLOSED("已关闭"),
    ;
}