package org.choppa.domain.squad

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.choppa.domain.chapter.ChapterController
import org.choppa.domain.iteration.IterationController
import org.choppa.domain.member.MemberController
import org.choppa.domain.tribe.TribeController
import org.choppa.utils.Color.Companion.toRGBAHex
import org.choppa.utils.ReverseRouter.Companion.queryComponent
import org.choppa.utils.ReverseRouter.Companion.route

class SquadSerializer(supportedClass: Class<Squad>? = null) : StdSerializer<Squad>(supportedClass) {
    override fun serialize(squad: Squad, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("id", route(SquadController::class, squad.id))
        gen.writeStringField("name", squad.name)
        gen.writeStringField("color", squad.color.toRGBAHex())
        gen.writeStringField("tribe", route(TribeController::class, squad.tribe.id))
        gen.writeArrayFieldStart("members")
        squad.members.forEach { gen.writeString(route(MemberController::class, it.id)) }
        gen.writeEndArray()
        gen.writeStringField(
            "chapters",
            queryComponent(ChapterController::class, ChapterController::listChapters, squad)
        )
        gen.writeStringField(
            "iterations",
            queryComponent(IterationController::class, IterationController::listIterations, squad)
        )
        gen.writeStringField("history", "history?squad=${squad.id}")
        gen.writeEndObject()
    }
}
