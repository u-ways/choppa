package org.choppa.domain.chapter

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.choppa.utils.Color.Companion.toRGBAHex

class ChapterSerializer(supportedClass: Class<Chapter>? = null) : StdSerializer<Chapter>(supportedClass) {
    override fun serialize(chapter: Chapter, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()

        gen.writeStringField("id", "chapters/${chapter.id}")
        gen.writeStringField("name", chapter.name)
        gen.writeStringField("color", chapter.color.toRGBAHex())
        gen.writeStringField("members", "members?chapter=${chapter.id}")

        gen.writeEndObject()
    }
}
