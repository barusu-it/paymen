package it.barusu.paymen.channel

import it.barusu.paymen.channel.config.ChannelSecret
import it.barusu.paymen.common.ChannelType
import it.barusu.paymen.util.JacksonUtils
import it.barusu.paymen.util.StringUtils
import org.apache.commons.io.IOUtils
import java.nio.charset.Charset

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
    }

}