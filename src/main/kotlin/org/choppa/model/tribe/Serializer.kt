package org.choppa.model.tribe

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class Serializer(supportedClass: Class<Tribe>? = null) : StdSerializer<Tribe>(supportedClass) {
    override fun serialize(tribe: Tribe, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()

        gen.writeStringField("id", "tribes/${tribe.id}")
        gen.writeStringField("name", tribe.name)

        gen.writeStringField("squads", "squads?tribe=${tribe.id}")
        gen.writeStringField("iterations", "iterations?tribe=${tribe.id}")
        gen.writeStringField("history", "history?tribe=${tribe.id}")

        gen.writeEndObject()
    }
}
