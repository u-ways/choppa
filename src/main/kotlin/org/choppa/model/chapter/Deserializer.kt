package org.choppa.model.chapter

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import org.choppa.databind.BaseDeserializer
import org.choppa.exception.UnprocessableEntityException

class Deserializer(supportedClass: Class<Chapter>? = null) : BaseDeserializer<Chapter>(supportedClass) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Chapter {
        try {
            val node: JsonNode = parser.codec.readTree(parser)
            return when (node.size()) {
                0 -> Chapter(node.extractUUID())
                1 -> Chapter(node["id"].extractUUID())
                else -> Chapter(node["id"].extractUUID(), node["name"].textValue())
            }
        } catch (e: Exception) {
            throw UnprocessableEntityException("Unable to parse requested chapter entity.", e)
        }
    }
}
