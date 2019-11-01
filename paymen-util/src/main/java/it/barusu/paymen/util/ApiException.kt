package it.barusu.paymen.util

class ApiException : RuntimeException {

    companion object {
        const val DEFAULT_CODE: String = "999999"
    }

    var code: String
    var msg: String

    constructor(code: String = DEFAULT_CODE, msg: String) : super(msg) {
        this.code = code
        this.msg = msg
    }

    constructor(code: String = DEFAULT_CODE, msg: String, throwable: Throwable) : super(msg, throwable) {
        this.code = code
        this.msg = msg
    }

}