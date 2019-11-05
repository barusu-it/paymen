package it.barusu.paymen.cashier

import it.barusu.paymen.common.CashierType
import it.barusu.paymen.common.ChannelType

data class CashierPaymentGlobalConfig(
        var id: String? = null,
        var cashierType: CashierType? = null,
        var channelType: ChannelType? = null,
        var name: String? = null,
        var logo: String? = null
)