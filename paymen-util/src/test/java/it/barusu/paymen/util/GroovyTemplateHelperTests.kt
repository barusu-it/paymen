package it.barusu.paymen.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class GroovyTemplateHelperTests {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @DisplayName("test simple groovy template")
    @Test
    fun testSimple() {
        val helper = GroovyTemplateHelper()
        val binding = mutableMapOf("username" to "Barusu")
        val content = "Hello, \$username"
        val result = helper.renderTemplate(content, binding)
        log.info("result: $result!")
        assertThat(result).isNotNull().contains("Barusu")
    }
}