package it.barusu.paymen.util.xstream

enum class OrdinalType(val description: String) {
    UNORDERED("无序"),
    ASCII("美国信息交换标准代码"),
    LINKED("维持自身顺序"),
    ;
}