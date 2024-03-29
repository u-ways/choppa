package app.choppa.domain.squad

import app.choppa.domain.base.BaseSerializer
import app.choppa.domain.base.BaseSerializer.QueryType.CHAPTERS
import app.choppa.domain.tribe.TribeController
import app.choppa.utils.Color.Companion.toRGBAHex
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider

class SquadSerializer(
    supportedClass: Class<Squad>? = null
) : BaseSerializer<Squad>(supportedClass) {
    override fun serialize(squad: Squad, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField(squad::id.name, reverseRouter.route(SquadController::class, squad.id))
        gen.writeStringField(squad::name.name, squad.name)
        gen.writeStringField(squad::color.name, squad.color.toRGBAHex())
        gen.writeStringField(squad::tribe.name, reverseRouter.route(TribeController::class, squad.tribe.id))
        gen.writeObjectField(squad::members.name, squad.members)
        gen.writeQueryField(CHAPTERS, squad)
        gen.writeEndObject()
    }
}
