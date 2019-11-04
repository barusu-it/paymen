package it.barusu.paymen.channel

import it.barusu.paymen.channel.config.ChannelSecret
import it.barusu.paymen.common.ChannelType
import it.barusu.paymen.util.JacksonUtils
import it.barusu.paymen.util.StringUtils
import org.apache.commons.io.IOUtils
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import java.nio.charset.Charset
import java.security.SecureRandom

abstract class ChannelTestsUtils {
    companion object {
        private const val DEFAULT_CHANNEL_NO = "DEFAULT"
        private const val DEFAULT_ENVIRONMENT = "dev"
        private const val BASE_DIRECTION = "channel-secret"
        private const val SUFFIX = ".json"

        @JvmStatic
        private val SEPARATOR = System.getProperty("file.separator")

        @JvmStatic
        private val ENVIRONMENT = System.getProperty("env", DEFAULT_ENVIRONMENT)

        @JvmStatic
        fun getSecret(type: ChannelType, channelNo: String = DEFAULT_CHANNEL_NO): ChannelSecret {
            val filename = BASE_DIRECTION + SEPARATOR + ENVIRONMENT + SEPARATOR +
                    type.name + StringUtils.DASH + channelNo + SUFFIX
            val content = IOUtils.toString(Thread.currentThread().contextClassLoader
                    .getResourceAsStream(filename), Charset.forName(StringUtils.UTF_8))
            return JacksonUtils.parse(content, ChannelSecret::class.java)
        }

        @JvmStatic
        fun httpClient(): HttpClient {
            return HttpClients.custom()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(30000)
                            .setConnectionRequestTimeout(30000)
                            .setSocketTimeout(30000)
                            .build())
                    .setSSLContext(SSLContextBuilder.create()
                            .setProtocol("TLS")
                            .loadTrustMaterial(null, TrustSelfSignedStrategy())
                            .setSecureRandom(SecureRandom())
                            .build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier())
                    .build()
        }
    }

}