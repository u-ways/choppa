package org.choppa.model.tribe

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import org.choppa.databind.BaseDeserializer
import org.choppa.exception.UnprocessableEntityException

class Deserializer(supportedClass: Class<Tribe>? = null) : BaseDeserializer<Tribe>(supportedClass) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Tribe {
        try {
            val node: JsonNode = parser.codec.readTree(parser)
            return when (node.size()) {
                0 -> Tribe(node.extractUUID())
                1 -> Tribe(node["id"].extractUUID())
                else -> {
                    val id = node["id"].extractUUID()
                    val name = node["name"].textValue()
                    Tribe(id, name)
                }
            }
        } catch (e: Exception) {
            throw UnprocessableEntityException("Unable to parse requested tribe entity.", e)
        }
    }
}
