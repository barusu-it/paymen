package it.barusu.paymen.util

import java.nio.charset.StandardCharsets

class StringUtils {
    companion object {
        @JvmStatic
        val UTF_8: String = StandardCharsets.UTF_8.name()

        @JvmStatic
        val MERGE_LINE_REGEX = Regex("^\\s+|\\s+\$|\\r|\\n")

        const val EQUAL_SIGN = "="
        const val AMPERSAND = "&"
        const val EMPTY = ""
        const val DASH = "-"
        const val SLASH = "/"
        const val COLON = ":"
        const val DATE = "yyyMMdd"
        const val DATETIME = "yyyMMddHHmmss"

        @JvmStatic
        fun pair(data: Map<String, String>): String = data.entries.stream()
                .map { it.key + EQUAL_SIGN + it.value }
                .reduce { t: String?, u: String? -> t + AMPERSAND + u }
                .orElse(AMPERSAND)


    }
}
