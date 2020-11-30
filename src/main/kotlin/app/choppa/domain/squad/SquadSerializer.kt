package app.choppa.domain.squad

import app.choppa.domain.base.BaseSerializer
import app.choppa.domain.base.BaseSerializer.QueryType.CHAPTERS
import app.choppa.domain.base.BaseSerializer.QueryType.HISTORY
import app.choppa.domain.base.BaseSerializer.QueryType.ITERATIONS
import app.choppa.domain.member.MemberController
import app.choppa.domain.tribe.TribeController
import app.choppa.utils.Color.Companion.toRGBAHex
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider

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
        gen.writeQueryField(CHAPTERS, squad)
        gen.writeQueryField(ITERATIONS, squad)
        gen.writeQueryField(HISTORY, squad)
        gen.writeEndObject()
    }
}
