package app.choppa.domain.chapter

import app.choppa.domain.base.BaseDeserializer
import app.choppa.exception.UnprocessableEntityException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode

class ChapterDeserializer(supportedClass: Class<Chapter>? = null) : BaseDeserializer<Chapter>(supportedClass) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Chapter {
        try {
            val node: JsonNode = parser.codec.readTree(parser)
            return when (node.size()) {
                0 -> Chapter(node.extractUUID())
                1 -> Chapter(node["id"].extractUUID())
                else -> {
                    val id = node["id"].extractUUID()
                    val name = node["name"].textValue()
                    val color = node.extractColor()
                    Chapter(id, name, color)
                }
            }
        } catch (e: Exception) {
            throw UnprocessableEntityException("Unable to parse requested chapter entity.", e)
        }
    }
}
