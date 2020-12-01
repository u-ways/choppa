package app.choppa.domain.history

import app.choppa.domain.base.BaseSerializer
import app.choppa.domain.iteration.IterationController
import app.choppa.domain.member.MemberController
import app.choppa.domain.squad.SquadController
import app.choppa.domain.tribe.TribeController
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider

class HistorySerializer(
    supportedClass: Class<History>? = null
) : BaseSerializer<History>(supportedClass) {
    override fun serialize(history: History, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField(history::iteration.name, reverseRouter.route(IterationController::class, history.iteration.id))
        gen.writeStringField(history::tribe.name, reverseRouter.route(TribeController::class, history.tribe.id))
        gen.writeStringField(history::squad.name, reverseRouter.route(SquadController::class, history.squad.id))
        gen.writeStringField(history::member.name, reverseRouter.route(MemberController::class, history.member.id))
        gen.writeNumberField(history::createDate.name, history.createDate.toEpochMilli())
        gen.writeEndObject()
    }
}
