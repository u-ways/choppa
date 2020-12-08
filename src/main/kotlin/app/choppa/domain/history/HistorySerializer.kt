package app.choppa.domain.history

import app.choppa.domain.base.BaseSerializer
import app.choppa.domain.chapter.ChapterController
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
        gen.writeStringField(
            history::iteration.name,
            history.iteration?.id?.run {
                reverseRouter.route(IterationController::class, this)
            }
        )
        gen.writeStringField(
            history::tribe.name,
            history.tribe?.id?.run {
                reverseRouter.route(TribeController::class, this)
            }
        )
        gen.writeStringField(
            history::squad.name,
            history.squad?.id?.run {
                reverseRouter.route(SquadController::class, this)
            }
        )
        gen.writeStringField(
            history::member.name,
            history.member?.id?.run {
                reverseRouter.route(MemberController::class, this)
            }
        )
        gen.writeStringField(
            history::chapter.name,
            history.chapter?.id?.run {
                reverseRouter.route(ChapterController::class, this)
            }
        )
        gen.writeNumberField(history::createDate.name, history.createDate.toEpochMilli())
        gen.writeEndObject()
    }
}
