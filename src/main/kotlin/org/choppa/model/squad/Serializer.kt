package org.choppa.model.squad

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class Serializer(supportedClass: Class<Squad>? = null) : StdSerializer<Squad>(supportedClass) {
    override fun serialize(squad: Squad, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()

        gen.writeStringField("id", "squads/${squad.id}")
        gen.writeStringField("name", squad.name)
        gen.writeStringField("tribe", "tribes/${squad.tribe.id}")

        gen.writeArrayFieldStart("members")
        squad.members.forEach { gen.writeString("members/${it.id}") }
        gen.writeEndArray()

        gen.writeStringField("iterations", "iterations?squad=${squad.id}")
        gen.writeStringField("history", "history?squad=${squad.id}")

        gen.writeEndObject()
    }
}
