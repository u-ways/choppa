package org.choppa.databind

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.util.UUID

abstract class BaseDeserializer<T>(supportedClass: Class<T>?) : StdDeserializer<T>(supportedClass) {
    internal val mapper = ObjectMapper()
    internal fun JsonNode.extractUUID(): UUID {
        return UUID.fromString(
            this.textValue().let {
                val uuidLength = 36
                if (it.length > uuidLength) {
                    val startOfUUIDIndex = it.indexOf('/') + 1
                    it.substring(startOfUUIDIndex)
                } else it
            }
        )
    }
}
