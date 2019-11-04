package it.barusu.paymen.channel.process.wechat

import com.thoughtworks.xstream.XStream
import it.barusu.paymen.channel.Request
import it.barusu.paymen.channel.process.wechat.WechatConverter.Companion.CODE_SUCCESS
import it.barusu.paymen.util.ApiException
import it.barusu.paymen.util.SecurityUtils
import it.barusu.paymen.util.StringUtils
import it.barusu.paymen.util.StringUtils.Companion.AMPERSAND
import it.barusu.paymen.util.StringUtils.Companion.EQUAL_SIGN
import it.barusu.paymen.util.xstream.NestedMapConverter
import it.barusu.paymen.util.xstream.OrdinalType
import org.apache.commons.codec.binary.Hex
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.Charset
import java.util.*
import java.util.regex.Pattern

class WechatCryptor {

    companion object {
        @JvmStatic
        private val log: Logger = LoggerFactory.getLogger(this::class.java)

        const val XML_NODE_ROOT = "xml"
        const val XML_NODE_SIGNATURE = "sign"
        const val XML_NODE_CODE = "result_code"
        const val XML_NAME_key = "key"

        private val SIGNATURE_REPLACEMENT: Pattern = Pattern.compile("(?<=<sign>)(.*?)(?=</sign>)")
    }

    private val xStream = XStream()

    init {
        XStream.setupDefaultSecurity(xStream)
        xStream.allowTypes(arrayOf(TreeMap::class.java))
        xStream.alias(XML_NODE_ROOT, TreeMap::class.java)
        xStream.registerConverter(NestedMapConverter(OrdinalType.ASCII))
    }

    fun sign(content: String, request: Request): String {
        @Suppress("UNCHECKED_CAST")
        val elements = xStream.fromXML(content) as TreeMap<String, String>
        elements.remove(XML_NODE_SIGNATURE)

        val pairs = StringUtils.pair(elements)
        val data = pairs + AMPERSAND + XML_NAME_key + EQUAL_SIGN + request.secret.secretKey
        val signature = Hex.encodeHexString(SecurityUtils.digest(request.secret.signatureAlgorithm!!,
                data.toByteArray(Charset.forName(request.secret.encoding)))).toUpperCase()

        return SIGNATURE_REPLACEMENT.matcher(content).replaceFirst(signature)
    }

    fun verify(content: String, request: Request) {

        @Suppress("UNCHECKED_CAST")
        val elements = xStream.fromXML(content) as MutableMap<String, String>

        if (CODE_SUCCESS != elements[XML_NODE_CODE]) {
            return
        }

        val originalSignature = elements.remove(XML_NODE_SIGNATURE)
        val pairs = StringUtils.pair(elements)
        val data = pairs + AMPERSAND + XML_NAME_key + EQUAL_SIGN + request.secret.secretKey
        val signature = Hex.encodeHexString(SecurityUtils.digest(request.secret.signatureAlgorithm!!,
                data.toByteArray(Charset.forName(request.secret.encoding)))).toUpperCase()
        if (!signature.equals(originalSignature, true)) {
            log.error("The invalid signature: '$originalSignature' was found in response '$data'.")
            throw ApiException(msg = "Invalid signature was found in response")
        }

    }
}