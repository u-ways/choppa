package org.choppa.domain.iteration

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import org.choppa.domain.base.BaseDeserializer
import org.choppa.exception.UnprocessableEntityException
import java.time.Instant.ofEpochMilli

class IterationDeserializer(supportedClass: Class<Iteration>? = null) : BaseDeserializer<Iteration>(supportedClass) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Iteration {
        try {
            val node: JsonNode = parser.codec.readTree(parser)
            return when (node.size()) {
                0 -> Iteration(node.extractUUID())
                1 -> Iteration(node["id"].extractUUID())
                2 -> Iteration(node["id"].extractUUID(), node["number"].intValue())
                3 -> Iteration(
                    node["id"].extractUUID(),
                    node["number"].intValue(),
                    ofEpochMilli(node["startDate"].longValue())
                )
                else -> {
                    val number = node["number"].intValue()
                    val startDate = ofEpochMilli(node["startDate"].longValue())
                    val endDate = ofEpochMilli(node["endDate"].longValue())
                    Iteration(node["id"].extractUUID(), number, startDate, endDate)
                }
            }
        } catch (e: Exception) {
            throw UnprocessableEntityException("Unable to parse requested iteration entity.", e)
        }
    }
}
