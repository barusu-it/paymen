package it.barusu.paymen.channel.process.wechat

import it.barusu.paymen.channel.AbstractProcessor
import it.barusu.paymen.channel.FreemarkerHelperBuilder
import it.barusu.paymen.channel.Processor.Companion.NAME_SUFFIX
import it.barusu.paymen.channel.Request
import it.barusu.paymen.channel.Response
import it.barusu.paymen.common.ChannelType
import it.barusu.paymen.common.RequestType
import it.barusu.paymen.util.ApiException
import it.barusu.paymen.util.SecurityUtils
import it.barusu.paymen.util.StringUtils.Companion.DASH
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import org.apache.http.ssl.SSLContextBuilder
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class WechatProcessor(private val httpClient: HttpClient,
                      private val converter: WechatConverter = WechatConverter(FreemarkerHelperBuilder.build()),
                      private val cryptor: WechatCryptor = WechatCryptor()) : AbstractProcessor() {

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(this::class.java)

        const val URL_UNIFIED_ORDER = "/unifiedorder"
        const val URL_ORDER_QUERY = "/orderquery"

        @JvmStatic
        val BEAN_NAME = ChannelType.WECHAT.name + NAME_SUFFIX

        @JvmStatic
        private val HEADER_XML = BasicHeader("Content-Type", "text/xml; charset=utf8")

        @JvmStatic
        private val httpClients: Map<String, HttpClient> = ConcurrentHashMap()
    }

    override fun <T : Response> execute(request: Request): T {
        val requestString = converter.writeTo(request)
        val signedRequestString = cryptor.sign(requestString, request)
        log.info("signed string: $signedRequestString")

        request.content = signedRequestString
        val requestBase = compose(request, signedRequestString)
        val responseEntity = httpClient.execute(requestBase).entity
        val responseString = EntityUtils.toString(responseEntity, request.secret.encoding)
        log.info("response string: $responseString")
        request.responseContent = responseString

        cryptor.verify(responseString, request)
        val response: Response = converter.readFrom(responseString, request)
        response.content = responseString

        @Suppress("UNCHECKED_CAST")
        return response as T
    }

    private fun compose(request: Request, content: String): HttpRequestBase {
        val contextUrl: String = when (request.type) {
            RequestType.TRANSACTION -> URL_UNIFIED_ORDER
            RequestType.TRANSACTION_QUERY -> URL_ORDER_QUERY
            else -> throw ApiException(msg = "Request type '${request.type}' is not supported by Wechat.")
        }

        val post = HttpPost(request.secret.baseUrl + contextUrl)
        post.setHeader(HEADER_XML)
        post.entity = StringEntity(content, request.secret.encoding)
        return post
    }

    private fun getHttpClient(request: Request): HttpClient {
        val keyName = request.secret.channelType!!.name + DASH + request.secret.channelNo
        return httpClients.getOrDefault(keyName, buildHttpClient(request))

    }

    private fun buildHttpClient(request: Request): HttpClient {
        val keyStore = SecurityUtils.getKeyStore(request.secret.privateKeyType!!, request.secret.privateKey!!,
                request.secret.privateKeyPassword!!)

        return HttpClients
                .custom()
                .setDefaultRequestConfig(RequestConfig
                        .custom()
                        .setConnectTimeout(30000)
                        .setConnectionRequestTimeout(30000)
                        .setSocketTimeout(30000)
                        .build())
                .setSSLContext(SSLContextBuilder
                        .create()
                        .setProtocol("TLSv1")
                        .loadKeyMaterial(keyStore, request.secret.privateKeyPassword!!.toCharArray())
                        .build())
                .setSSLHostnameVerifier(SSLConnectionSocketFactory.getDefaultHostnameVerifier())
                .build()
    }

}