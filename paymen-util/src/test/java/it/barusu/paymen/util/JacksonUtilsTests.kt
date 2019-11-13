package it.barusu.paymen.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class JacksonUtilsTests {
    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @DisplayName("test parse")
    @Test
    fun testParse() {
        val text = "{\"name\": \"jessie\", age: 6}"

        val map: Map<*, *> = JacksonUtils.parse(text, Map::class.java)
        log.info("parsed result: $map")
        assertThat(map["name"]).isEqualTo("jessie")

        val anyMap: Map<String, Any> = JacksonUtils.parse(text, String::class.java, Any::class.java)
        log.info("parsed any map result: $anyMap")
        assertThat(anyMap["age"]).isEqualTo(6)

        val listText = "[\"jessie\",\"fei\"]"

        val list: List<String> = JacksonUtils.parseList(listText, String::class.java)
        log.info("parsed list result: $list")
        assertThat(list[1]).isEqualTo("fei")
    }

    @DisplayName("test convert")
    @Test
    fun testConvert() {
        val map = mutableMapOf<String, Any>("name" to "jessie", "age" to 6)

        val text = JacksonUtils.convert(map)
        log.info("text: $text")
        assertThat(text).contains("jessie")

        val convertedMap = JacksonUtils.convert(map, Map::class.java)
        log.info("converted map: $convertedMap")
        assertThat(convertedMap["age"]).isEqualTo(6)

    }

}