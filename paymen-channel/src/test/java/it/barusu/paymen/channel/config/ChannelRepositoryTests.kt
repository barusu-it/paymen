package it.barusu.paymen.channel.config

import it.barusu.paymen.channel.DataJpaTestConfiguration
import it.barusu.paymen.common.ChannelType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

// it could not use @DataJpaTest with Junit5, so use @SpringBootTest here.
@SpringBootTest(classes = [DataJpaTestConfiguration::class])
@ExtendWith(SpringExtension::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChannelRepositoryTests {

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @Autowired
    private lateinit var channelRepository: ChannelRepository

    @DisplayName("test save channel")
    @Test
    fun testSaveChannel() {
        var channel = Channel(
                channelNo = "WECHAT_DEFAULT",
                channelType = ChannelType.WECHAT,
                name = "W微信",
                description = "微信默认渠道")
        channel = channelRepository.save(channel)
        log.info("save channel: $channel")

        val query = channelRepository.findByChannelNo(channel.channelNo!!).get()
        log.info("query channel: $query")

        assertThat(query).isEqualTo(channel)
    }

}