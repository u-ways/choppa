package org.choppa.model.chapter

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.choppa.helpers.exception.UnprocessableEntityException
import java.util.UUID

class Deserializer(supportedClass: Class<Chapter>? = null) : StdDeserializer<Chapter>(supportedClass) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Chapter {
        val uniformURIAddition = Chapter::class.simpleName!!.toLowerCase().length + 2
        fun String.isUniformURI(): Boolean = this.length > 36

        try {
            val node: JsonNode = parser.codec.readTree(parser)
            val uuid = UUID.fromString(
                node.get("id").textValue().let {
                    if (it.isUniformURI()) it.substring(uniformURIAddition) else it
                }
            )
            val name = node.get("name").textValue()
            return Chapter(uuid, name)
        } catch (e: Exception) {
            throw UnprocessableEntityException("Unable to parse requested chapter entity.")
        }
    }
}
