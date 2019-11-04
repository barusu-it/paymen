package it.barusu.paymen.channel.process.wechat

import it.barusu.paymen.channel.ChannelTestsUtils
import it.barusu.paymen.channel.process.*
import it.barusu.paymen.channel.process.wechat.WechatConverter.Companion.CODE_SUCCESS
import it.barusu.paymen.common.ChannelType
import it.barusu.paymen.common.TransactionStatus
import it.barusu.paymen.common.TransactionType
import it.barusu.paymen.util.IdUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.LocalDateTime

class WechatProcessorTests {

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    private val processor = WechatProcessor(ChannelTestsUtils.httpClient())
    private val secret = ChannelTestsUtils.getSecret(ChannelType.WECHAT)

    @DisplayName("test transaction")
    @Test
    fun testTransaction() {
        val request = TransactionRequest()
        request.secret = secret
        request.orderNo = IdUtils.uuid()
        request.transactionType = TransactionType.WITHHOLD
        request.transaction = Transaction(
                transactionNo = IdUtils.uuidWithoutDash(),
                transactionType = request.transactionType,
                description = "test transaction",
                amount = BigDecimal("1"),
                expiredTime = LocalDateTime.now().plusHours(1L)
        )

        val response: TransactionResponse = processor.execute(request)

        log.info("response: $response")

        assertThat(response).isNotNull
        assertThat(response.transaction!!.code).isEqualTo(CODE_SUCCESS)
        assertThat(response.transaction!!.status).isEqualTo(TransactionStatus.PROCESSING)
    }

    @DisplayName("test transaction query")
    @Test
    fun testTransactionQuery() {
        val request = TransactionQueryRequest()
        request.secret = secret
        request.transactionType = TransactionType.WITHHOLD
        request.transaction = Transaction(transactionNo = "a14f9fc9cfb34451b930b1530eb8ed1c")

        val response: TransactionQueryResponse = processor.execute(request)

        log.info("response: $response")

        assertThat(response).isNotNull
        assertThat(response.transaction!!.code).isEqualTo(CODE_SUCCESS)
        assertThat(response.transaction!!.status).isEqualTo(TransactionStatus.FAILED)

    }
}