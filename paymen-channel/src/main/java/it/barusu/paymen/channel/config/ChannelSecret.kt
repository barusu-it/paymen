package it.barusu.paymen.channel.config

import it.barusu.paymen.common.ChannelType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collation = "pm_channel_secret")
data class ChannelSecret(
        @Id
        var channelNo: String? = null,
        var channelType: ChannelType? = null,
        var baseUrl: String? = null,
        var callbackUrl: String? = null,
        var returnUrl: String? = null,
        var encoding: String? = null,
        var merchantNo: String? = null,
        var merchantName: String? = null,
        var username: String? = null,
        var password: String? = null,
        var privateKeyType: String? = null,
        var privateKey: String? = null,
        var privateKeyPassword: String? = null,
        var publicKeyType: String? = null,
        var publicKey: String? = null,
        var signatureAlgorithm: String? = null,
        var secretKeyType: String? = null,
        var secretKey: String? = null,
        var appId: String? = null)