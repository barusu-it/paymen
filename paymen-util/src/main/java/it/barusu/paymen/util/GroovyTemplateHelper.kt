package it.barusu.paymen.util

import groovy.text.SimpleTemplateEngine
import it.barusu.paymen.util.StringUtils.Companion.EMPTY
import it.barusu.paymen.util.StringUtils.Companion.MERGE_LINE_REGEX

class GroovyTemplateHelper {

    private val engine = SimpleTemplateEngine()

    fun renderTemplate(content: String, binding: Map<String, Any>): String =
            engine.createTemplate(content.replace(MERGE_LINE_REGEX, EMPTY)).make(binding).toString()


}