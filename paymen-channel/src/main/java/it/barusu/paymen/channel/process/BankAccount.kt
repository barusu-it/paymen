package it.barusu.paymen.channel.process

import it.barusu.paymen.common.BankCardType
import it.barusu.paymen.common.IdType

data class BankAccount(
        var bankAccountNo: String? = null,
        var bankAcronym: String? = null,
        var channelBankCode: String? = null,
        var bankAccountName: String? = null,
        var bankCardType: BankCardType? = null,
        var bankReservedPhone: String? = null,
        var cvv2: String? = null,
        var validThru: String? = null,
        var pinCode: String? = null,
        var idNo: String? = null,
        var idType: IdType? = IdType.ID_CARD)