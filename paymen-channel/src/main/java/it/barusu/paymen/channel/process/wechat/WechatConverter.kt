package it.barusu.paymen.channel.process.wechat

import com.thoughtworks.xstream.XStream
import freemarker.ext.beans.BeansWrapperBuilder
import freemarker.template.Configuration
import freemarker.template.Template
import it.barusu.paymen.channel.AbstractConverter
import it.barusu.paymen.channel.Request
import it.barusu.paymen.channel.process.*
import it.barusu.paymen.channel.process.wechat.mapper.ResponseMapper
import it.barusu.paymen.common.TransactionStatus
import it.barusu.paymen.util.DateUtils
import it.barusu.paymen.util.FreeMarkerHelper
import it.barusu.paymen.util.IdUtils
import java.time.LocalDate
import java.util.*

class WechatConverter(var freeMarkerHelper: FreeMarkerHelper) : AbstractConverter() {

    companion object {
        const val TEMPLATE_ATTRIBUTE_REQUEST = "request"
        const val CODE_SUCCESS = "SUCCESS"
        const val CODE_ORDER_NOT_EXIST = "ORDERNOTEXIST"

        @JvmStatic
        val CODES_FAILED: Set<String> = setOf("CLOSED", "REVOKED")

    }

    private val transactionTemplate: Template = freeMarkerHelper.configuration
            .getTemplate("WECHAT_Transaction.ftl")
    private val transactionQueryTemplate: Template = freeMarkerHelper.configuration
            .getTemplate("WECHAT_TransactionQuery.ftl")

    private val xStream: XStream = XStream()

    init {
        XStream.setupDefaultSecurity(xStream)
        xStream.allowTypes(arrayOf(ResponseMapper::class.java))
        xStream.processAnnotations(ResponseMapper::class.java)
        xStream.ignoreUnknownElements()
    }

    override fun from(request: TransactionRequest): String {
        val wrapper = BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build()
        val templateHashModel = wrapper.staticModels
        val data = mapOf<String, Any>(
                TEMPLATE_ATTRIBUTE_REQUEST to request,
                IdUtils::class.java.simpleName to templateHashModel[IdUtils::class.java.name],
                DateUtils::class.java.simpleName to templateHashModel[DateUtils::class.java.name]
        )

        return freeMarkerHelper.render(transactionTemplate, data)
    }

    override fun toTransactionResponse(content: String, request: Request): TransactionResponse {
        val mapper = xStream.fromXML(content) as ResponseMapper
        val transactionCode = if (Objects.equals(CODE_SUCCESS, mapper.resultCode))
            mapper.resultCode else mapper.errorCode
        val transactionStatus = if (Objects.equals(CODE_SUCCESS, mapper.resultCode))
            TransactionStatus.PROCESSING else TransactionStatus.FAILED

        val response = TransactionResponse()
        response.code = mapper.code
        response.message = mapper.message
        response.transaction = Transaction(
                status = transactionStatus,
                code = transactionCode,
                message = mapper.errorMessage,
                channelPrepayNo = mapper.prepayId,
                paymentCode = mapper.codeUrl,
                settlementDate = if (Objects.nonNull(mapper.finishedTime))
                    LocalDate.parse(mapper.finishedTime, DateUtils.DATE) else null)

        return response
    }

    override fun from(request: TransactionQueryRequest): String {
        val wrapper = BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build()
        val templateHashModel = wrapper.staticModels
        val data = mapOf<String, Any>(
                TEMPLATE_ATTRIBUTE_REQUEST to request,
                IdUtils::class.java.simpleName to templateHashModel[IdUtils::class.java.name]
        )

        return freeMarkerHelper.render(transactionQueryTemplate, data)
    }

    override fun toTransactionQueryResponse(content: String, request: Request): TransactionQueryResponse {
        val mapper = xStream.fromXML(content) as ResponseMapper
        val response = TransactionQueryResponse()
        response.code = mapper.code
        response.message = mapper.message
        response.transaction = Transaction(
                status = if (Objects.equals(CODE_SUCCESS, mapper.code) || mapper.resultCode!!.endsWith(CODE_SUCCESS)) {
                    when {
                        Objects.equals(CODE_SUCCESS, mapper.tradeState) -> TransactionStatus.SUCCEED
                        Objects.equals(CODE_ORDER_NOT_EXIST, mapper.errorCode) -> TransactionStatus.FAILED
                        CODES_FAILED.contains(mapper.tradeState) -> TransactionStatus.FAILED
                        else -> TransactionStatus.PROCESSING
                    }
                } else {
                    TransactionStatus.PROCESSING
                },
                code = if (mapper.errorCode.isNullOrEmpty())
                    mapper.resultCode else mapper.errorCode,
                message = mapper.errorMessage,
                settlementDate = if (Objects.nonNull(mapper.finishedTime))
                    LocalDate.parse(mapper.finishedTime, DateUtils.DATE) else null,
                channelPrepayNo = mapper.prepayId)

        return response
    }

    override fun from(request: TransactionNotificationRequest): String {
        return super.from(request)
    }


}