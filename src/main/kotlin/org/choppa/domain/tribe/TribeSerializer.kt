package org.choppa.domain.tribe

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.choppa.domain.chapter.ChapterController
import org.choppa.domain.history.HistoryController
import org.choppa.domain.iteration.IterationController
import org.choppa.domain.member.MemberController
import org.choppa.domain.squad.SquadController
import org.choppa.utils.Color.Companion.toRGBAHex
import org.choppa.utils.ReverseRouter
import org.springframework.beans.factory.annotation.Autowired

class TribeSerializer(
    supportedClass: Class<Tribe>? = null,
    @Autowired private val reverseRouter: ReverseRouter = ReverseRouter(),
) : StdSerializer<Tribe>(supportedClass) {

    override fun serialize(tribe: Tribe, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("id", reverseRouter.route(TribeController::class, tribe.id))
        gen.writeStringField("name", tribe.name)
        gen.writeStringField("color", tribe.color.toRGBAHex())
        gen.writeStringField(
            "chapters",
            reverseRouter.queryComponent(ChapterController::class, ChapterController::listChapters, tribe)
        )
        gen.writeStringField(
            "members",
            reverseRouter.queryComponent(MemberController::class, MemberController::listMembers, tribe)
        )
        gen.writeStringField(
            "squads",
            reverseRouter.queryComponent(SquadController::class, SquadController::listSquads, tribe)
        )
        gen.writeStringField(
            "iterations",
            reverseRouter.queryComponent(IterationController::class, IterationController::listIterations, tribe)
        )
        gen.writeStringField(
            "history",
            reverseRouter.queryComponent(HistoryController::class, HistoryController::listHistory, tribe)
        )
        gen.writeEndObject()
    }
}
