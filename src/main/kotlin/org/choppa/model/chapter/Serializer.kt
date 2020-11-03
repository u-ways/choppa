package org.choppa.model.chapter

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.choppa.model.Member

class Serializer(supportedClass: Class<Chapter>? = null) : StdSerializer<Chapter>(supportedClass) {
    override fun serialize(chapter: Chapter, gen: JsonGenerator, provider: SerializerProvider) {
        val entity = Chapter::class.simpleName!!.toLowerCase()
        val relatedEntityMember = Member::class.simpleName!!.toLowerCase()

        gen.writeStartObject()
        gen.writeStringField("id", "${entity}s/${chapter.id}")
        gen.writeStringField("name", chapter.name)
        gen.writeStringField("members", "${relatedEntityMember}s?$entity=${chapter.id}")
        gen.writeEndObject()
    }
}
