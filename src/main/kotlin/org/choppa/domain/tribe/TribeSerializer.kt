package org.choppa.domain.tribe

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.choppa.utils.Color.Companion.toRGBAHex

class TribeSerializer(supportedClass: Class<Tribe>? = null) : StdSerializer<Tribe>(supportedClass) {
    override fun serialize(tribe: Tribe, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()

        gen.writeStringField("id", "tribes/${tribe.id}")
        gen.writeStringField("name", tribe.name)
        gen.writeStringField("color", tribe.color.toRGBAHex())

        gen.writeStringField("squads", "squads?tribe=${tribe.id}")
        gen.writeStringField("iterations", "iterations?tribe=${tribe.id}")
        gen.writeStringField("history", "history?tribe=${tribe.id}")

        gen.writeEndObject()
    }
}
