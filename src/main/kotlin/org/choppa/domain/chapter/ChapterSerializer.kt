package org.choppa.domain.chapter

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.choppa.domain.member.MemberController
import org.choppa.utils.Color.Companion.toRGBAHex
import org.choppa.utils.ReverseRouter.Companion.queryComponent
import org.choppa.utils.ReverseRouter.Companion.route

class ChapterSerializer(supportedClass: Class<Chapter>? = null) : StdSerializer<Chapter>(supportedClass) {
    override fun serialize(chapter: Chapter, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("id", route(ChapterController::class, chapter.id))
        gen.writeStringField("name", chapter.name)
        gen.writeStringField("color", chapter.color.toRGBAHex())
        gen.writeStringField(
            "members",
            queryComponent(MemberController::class, MemberController::listMembers, chapter))
        gen.writeEndObject()
    }
}
