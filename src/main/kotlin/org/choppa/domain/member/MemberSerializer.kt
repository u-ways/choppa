package org.choppa.domain.member

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.choppa.domain.chapter.ChapterController
import org.choppa.domain.iteration.IterationController
import org.choppa.domain.squad.SquadController
import org.choppa.utils.ReverseRouter
import org.springframework.beans.factory.annotation.Autowired

class MemberSerializer(
    supportedClass: Class<Member>? = null,
    @Autowired private val reverseRouter: ReverseRouter = ReverseRouter(),
) : StdSerializer<Member>(supportedClass) {

    override fun serialize(member: Member, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("id", reverseRouter.route(MemberController::class, member.id))
        gen.writeStringField("name", member.name)
        gen.writeStringField("chapter", reverseRouter.route(ChapterController::class, member.chapter.id))
        gen.writeStringField(
            "squads",
            reverseRouter.queryComponent(SquadController::class, SquadController::listSquads, member)
        )
        gen.writeStringField(
            "iterations",
            reverseRouter.queryComponent(IterationController::class, IterationController::listIterations, member)
        )
        gen.writeStringField("history", "history?member=${member.id}")
        gen.writeEndObject()
    }
}
