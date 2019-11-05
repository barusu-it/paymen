package it.barusu.paymen.cashier

import it.barusu.paymen.common.CashierType
import it.barusu.paymen.common.ChannelType

data class CashierPaymentRule(
        var id: String,
        var paymentId: Long,
        var merchantId: Long,
        var cashierType: CashierType? = null,
        var channelType: ChannelType? = null,
        var rules: String? = null
)