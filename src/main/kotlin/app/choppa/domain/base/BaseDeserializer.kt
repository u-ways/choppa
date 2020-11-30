package app.choppa.domain.base

import app.choppa.utils.Color
import app.choppa.utils.Color.Companion.toRGBAInt
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.util.UUID
import java.util.UUID.fromString

abstract class BaseDeserializer<T>(supportedClass: Class<T>?) : StdDeserializer<T>(supportedClass) {
    internal val mapper = ObjectMapper()

    internal fun JsonNode.extractUUID(): UUID = fromString(
        this.textValue().let {
            val uuidLength = 36
            if (it.length > uuidLength) {
                val startOfUUIDIndex = it.indexOf('/') + 1
                it.substring(startOfUUIDIndex)
            } else it
        }
    )

    internal fun JsonNode.extractColor(): Int =
        when (val child: JsonNode? = this["color"]) {
            null -> Color.GREY
            else -> child.textValue().toRGBAInt()
        }
}
