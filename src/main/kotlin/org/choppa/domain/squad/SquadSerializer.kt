package org.choppa.domain.squad

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import org.choppa.domain.base.BaseSerializer
import org.choppa.domain.base.BaseSerializer.QueryType.CHAPTERS
import org.choppa.domain.base.BaseSerializer.QueryType.HISTORY
import org.choppa.domain.base.BaseSerializer.QueryType.ITERATIONS
import org.choppa.domain.member.MemberController
import org.choppa.domain.tribe.TribeController
import org.choppa.utils.Color.Companion.toRGBAHex

class SquadSerializer(
    supportedClass: Class<Squad>? = null
) : BaseSerializer<Squad>(supportedClass) {
    override fun serialize(squad: Squad, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("id", reverseRouter.route(SquadController::class, squad.id))
        gen.writeStringField("name", squad.name)
        gen.writeStringField("color", squad.color.toRGBAHex())
        gen.writeStringField("tribe", reverseRouter.route(TribeController::class, squad.tribe.id))
        gen.writeArrayFieldStart("members")
        squad.members.forEach { gen.writeString(reverseRouter.route(MemberController::class, it.id)) }
        gen.writeEndArray()
        gen.queryComponent(CHAPTERS, squad)
        gen.queryComponent(ITERATIONS, squad)
        gen.queryComponent(HISTORY, squad)
        gen.writeEndObject()
    }
}
