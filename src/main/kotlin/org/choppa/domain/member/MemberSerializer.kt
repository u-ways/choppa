package org.choppa.domain.member

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class MemberSerializer(supportedClass: Class<Member>? = null) : StdSerializer<Member>(supportedClass) {
    override fun serialize(member: Member, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()

        gen.writeStringField("id", "members/${member.id}")
        gen.writeStringField("name", member.name)
        gen.writeStringField("chapter", "chapters/${member.chapter.id}")

        gen.writeStringField("squads", "squads?member=${member.id}")
        gen.writeStringField("iterations", "iterations?member=${member.id}")
        gen.writeStringField("history", "history?member=${member.id}")

        gen.writeEndObject()
    }
}
