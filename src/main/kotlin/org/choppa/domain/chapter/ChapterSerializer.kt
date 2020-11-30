package org.choppa.domain.chapter

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import org.choppa.domain.base.BaseSerializer
import org.choppa.domain.base.BaseSerializer.QueryType.MEMBERS
import org.choppa.utils.Color.Companion.toRGBAHex

class ChapterSerializer(
    supportedClass: Class<Chapter>? = null
) : BaseSerializer<Chapter>(supportedClass) {
    override fun serialize(chapter: Chapter, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("id", reverseRouter.route(ChapterController::class, chapter.id))
        gen.writeStringField("name", chapter.name)
        gen.writeStringField("color", chapter.color.toRGBAHex())
        gen.queryComponent(MEMBERS, chapter)
        gen.writeEndObject()
    }
}

