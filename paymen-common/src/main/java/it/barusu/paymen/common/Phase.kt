package it.barusu.paymen.common

enum class Phase(val description: String) {
    CREATE("创建"),
    ORDER("下单"),
    SUBMIT("提交"),
    EXECUTE("执行"),
    ;
}