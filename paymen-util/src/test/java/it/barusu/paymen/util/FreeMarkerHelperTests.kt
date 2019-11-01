package it.barusu.paymen.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class FreeMarkerHelperTests {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @DisplayName("test freemarker")
    @Test
    fun testHelloWorld() {
        val helper = FreeMarkerHelper(arrayOf("/tests/freemarker"))
        val template = helper.configuration.getTemplate("hello-world.ftl")

        val username = "Jerry"
        val data = mutableMapOf<String, Any>("username" to username)
        val content = helper.render(template, data)

        log.info("render content: $content")
        assertThat(content).isNotNull().isEqualTo("Hi, ${username}, Hello World!")

    }
}