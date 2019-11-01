package it.barusu.paymen.channel

import it.barusu.paymen.util.FreeMarkerHelper
import it.barusu.paymen.util.StringUtils
import org.apache.commons.io.FilenameUtils
import org.slf4j.LoggerFactory
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

class FreemarkerParser {

    companion object {
        @JvmStatic
        val log = LoggerFactory.getLogger(this::class.java)

        @JvmStatic
        val DEFAULT_PATH = "template/freemarker"

        @JvmStatic
        val DEFAULT_PATH_PATTERN = "classpath*:$DEFAULT_PATH/*"

    }

    var resolver = PathMatchingResourcePatternResolver()
    var pathPattern: String
    var templatePaths: Array<String>
    var templateHelper: FreeMarkerHelper

    constructor() : this(DEFAULT_PATH_PATTERN)

    constructor(pathPattern: String) {
        this.pathPattern = pathPattern
        this.templatePaths = this.resolver.getResources(this.pathPattern).toList().stream()
                .filter { it.url.path.endsWith(StringUtils.SLASH) }
                .map {
                    StringUtils.SLASH + DEFAULT_PATH + StringUtils.SLASH +
                            FilenameUtils.getName(it.url.path.removeSuffix(StringUtils.SLASH))
                }
                .toArray<String> { length -> arrayOfNulls(length) }
        log.info("template parser initializing with ${templatePaths.contentToString()}")
        this.templateHelper = FreeMarkerHelper(this.templatePaths)
    }


}