package it.barusu.paymen.channel.config

import it.barusu.paymen.channel.ChannelTestsUtils
import it.barusu.paymen.channel.ChannelTestsUtils.Companion.DEFAULT_CHANNEL_NO
import it.barusu.paymen.channel.MongodbTestConfiguration
import it.barusu.paymen.common.ChannelType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(classes = [MongodbTestConfiguration::class])
@ExtendWith(SpringExtension::class)
class ChannelSecretRepositoryTests {

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @Autowired
    private lateinit var channelSecretRepository: ChannelSecretRepository

    @DisplayName("test save channel secret")
    @Test
    fun testSaveChannelSecret() {
        val secret: ChannelSecret = ChannelTestsUtils.getSecret(ChannelType.WECHAT)

        channelSecretRepository.save(secret)
        log.info("save channel secret: $secret")

        val query = channelSecretRepository.findById(secret.channelNo ?: DEFAULT_CHANNEL_NO)
        log.info("query channel secret: $query")

        assertThat(query.get()).isEqualTo(secret)
    }

}