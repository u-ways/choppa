package org.choppa.domain.chapter

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.choppa.domain.member.MemberController
import org.choppa.utils.Color.Companion.toRGBAHex
import org.choppa.utils.ReverseRouter
import org.springframework.beans.factory.annotation.Autowired

class ChapterSerializer(
    supportedClass: Class<Chapter>? = null,
    @Autowired private val reverseRouter: ReverseRouter = ReverseRouter(),
) : StdSerializer<Chapter>(supportedClass) {

    override fun serialize(chapter: Chapter, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("id", reverseRouter.route(ChapterController::class, chapter.id))
        gen.writeStringField("name", chapter.name)
        gen.writeStringField("color", chapter.color.toRGBAHex())
        gen.writeStringField(
            "members",
            reverseRouter.queryComponent(MemberController::class, MemberController::listMembers, chapter)
        )
        gen.writeEndObject()
    }
}
