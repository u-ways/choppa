package org.choppa.domain.tribe

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.choppa.domain.chapter.ChapterController
import org.choppa.domain.iteration.IterationController
import org.choppa.domain.member.MemberController
import org.choppa.domain.squad.SquadController
import org.choppa.utils.Color.Companion.toRGBAHex
import org.choppa.utils.ReverseRouter.Companion.queryComponent
import org.choppa.utils.ReverseRouter.Companion.route

class TribeSerializer(supportedClass: Class<Tribe>? = null) : StdSerializer<Tribe>(supportedClass) {
    override fun serialize(tribe: Tribe, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("id", route(TribeController::class, tribe.id))
        gen.writeStringField("name", tribe.name)
        gen.writeStringField("color", tribe.color.toRGBAHex())
        gen.writeStringField(
            "chapters",
            queryComponent(ChapterController::class, ChapterController::listChapters, tribe)
        )
        gen.writeStringField(
            "members",
            queryComponent(MemberController::class, MemberController::listMembers, tribe)
        )
        gen.writeStringField(
            "squads",
            queryComponent(SquadController::class, SquadController::listSquads, tribe)
        )
        gen.writeStringField(
            "iterations",
            queryComponent(IterationController::class, IterationController::listIterations, tribe)
        )
        gen.writeStringField("history", "history?tribe=${tribe.id}")
        gen.writeEndObject()
    }
}
