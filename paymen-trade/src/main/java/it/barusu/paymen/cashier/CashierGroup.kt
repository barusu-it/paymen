package it.barusu.paymen.cashier

import it.barusu.paymen.common.CashierType
import it.barusu.paymen.common.Status
import java.time.LocalDateTime

data class CashierGroup(
        var id: Long? = null,
        var version: Long? = null,
        var merchantId: Long? = null,
        var cashierType: CashierType? = null,
        var groupName: String? = null,
        var description: String? = null,
        var logo: String? = null,
        var createdTime: LocalDateTime? = null,
        var updatedTime: LocalDateTime? = null,
        var status: Status? = null
)