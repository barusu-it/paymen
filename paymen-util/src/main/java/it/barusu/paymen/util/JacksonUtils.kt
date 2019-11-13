package it.barusu.paymen.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.type.MapType
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class JacksonUtils {

    companion object {
        @JvmStatic
        val DEFAULT_OBJECT_MAPPER = initObjectMapper()

        private fun initObjectMapper(): ObjectMapper {
            // use jacksonObjectMapper to register kotlin support automatically.
            val objectMapper = jacksonObjectMapper()
            objectMapper.registerModules(Jdk8Module(), JavaTimeModule())

            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)

            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

            return objectMapper
        }

        fun <T> parse(text: String, targetType: Class<T>): T {
            return DEFAULT_OBJECT_MAPPER.readValue(text, targetType)
        }

        fun <T : Map<*, *>, K, V> parse(text: String, targetType: Class<T>,
                                        keyType: Class<K>, valueType: Class<V>): Map<K, V> {
            val javaType: MapType = DEFAULT_OBJECT_MAPPER.typeFactory.constructMapType(targetType, keyType, valueType)
            return DEFAULT_OBJECT_MAPPER.readValue(text, javaType)
        }

        fun <K, V> parse(text: String, keyType: Class<K>, valueType: Class<V>): Map<K, V> =
                parse(text, Map::class.java, keyType, valueType)

        fun <T> parseList(text: String, targetType: Class<T>): List<T> {
            val listType: JavaType = DEFAULT_OBJECT_MAPPER.typeFactory
                    .constructParametricType(ArrayList::class.java, targetType)
            return DEFAULT_OBJECT_MAPPER.readValue(text, listType)
        }

        fun convert(any: Any): String {
            return DEFAULT_OBJECT_MAPPER.writeValueAsString(any)
        }

        fun <T> convert(any: Any, targetType: Class<T>): T {
            return DEFAULT_OBJECT_MAPPER.convertValue(any, targetType)
        }

    }
}