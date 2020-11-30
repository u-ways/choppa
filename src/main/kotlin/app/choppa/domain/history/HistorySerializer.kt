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
        gen.writeStringField("iteration", reverseRouter.route(IterationController::class, history.iteration.id))
        gen.writeStringField("tribe", reverseRouter.route(TribeController::class, history.tribe.id))
        gen.writeStringField("squad", reverseRouter.route(SquadController::class, history.squad.id))
        gen.writeStringField("member", reverseRouter.route(MemberController::class, history.member.id))
        gen.writeNumberField("createDate", history.createDate.toEpochMilli())
        gen.writeEndObject()
    }
}
