package it.barusu.paymen.common

enum class Status(val description: String) {
    @Suppress("SpellCheckingInspection")
    ACTIVED("已激活"),
    SUSPENDED("已挂起"),
    ;
}