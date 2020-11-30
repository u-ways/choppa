package app.choppa.domain.chapter

import app.choppa.domain.base.BaseSerializer
import app.choppa.domain.base.BaseSerializer.QueryType.MEMBERS
import app.choppa.utils.Color.Companion.toRGBAHex
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider

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
