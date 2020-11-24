package org.choppa.domain.tribe

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import org.choppa.domain.base.BaseDeserializer
import org.choppa.exception.UnprocessableEntityException

class TribeDeserializer(supportedClass: Class<Tribe>? = null) : BaseDeserializer<Tribe>(supportedClass) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Tribe {
        try {
            val node: JsonNode = parser.codec.readTree(parser)
            return when (node.size()) {
                0 -> Tribe(node.extractUUID())
                1 -> Tribe(node["id"].extractUUID())
                else -> {
                    val id = node["id"].extractUUID()
                    val name = node["name"].textValue()
                    val color = node.extractColor()
                    Tribe(id, name, color)
                }
            }
        } catch (e: Exception) {
            throw UnprocessableEntityException("Unable to parse requested tribe entity.", e)
        }
    }
}
