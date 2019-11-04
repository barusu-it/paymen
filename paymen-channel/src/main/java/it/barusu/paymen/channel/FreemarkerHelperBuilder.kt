package it.barusu.paymen.channel

import it.barusu.paymen.util.FreeMarkerHelper
import it.barusu.paymen.util.StringUtils
import org.apache.commons.io.FilenameUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

class FreemarkerHelperBuilder {

    companion object {
        @JvmStatic
        val log: Logger = LoggerFactory.getLogger(this::class.java)

        const val DEFAULT_PATH = "template/freemarker"
        const val DEFAULT_PATH_PATTERN = "classpath*:$DEFAULT_PATH/*"

        @JvmStatic
        val resolver = PathMatchingResourcePatternResolver()

        @JvmStatic
        fun build(pathPattern: String = DEFAULT_PATH_PATTERN): FreeMarkerHelper {
            val templatePaths = resolver.getResources(pathPattern).toList().stream()
                    .filter { it.url.path.endsWith(StringUtils.SLASH) }
                    .map {
                        StringUtils.SLASH + DEFAULT_PATH + StringUtils.SLASH +
                                FilenameUtils.getName(it.url.path.removeSuffix(StringUtils.SLASH))
                    }
                    .toArray<String> { length -> arrayOfNulls(length) }
            log.info("template parser initializing with ${with(templatePaths) { contentToString() }}")
            return FreeMarkerHelper(templatePaths)
        }
    }

}