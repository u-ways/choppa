package app.choppa.domain.squad.history

import app.choppa.domain.base.BaseSerializer
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberController
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadController
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider

class SquadMemberHistorySerializer(
    supportedClass: Class<SquadMemberHistory>? = null
) : BaseSerializer<SquadMemberHistory>(supportedClass) {
    override fun serialize(revision: SquadMemberHistory, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeNumberField(SquadMemberHistory::createDate.name, revision.createDate.toEpochMilli())
        gen.writeObjectFieldStart(SquadMemberHistory::squad.name)
        gen.writeStringField(Squad::id.name, reverseRouter.route(SquadController::class, revision.squad.id))
        gen.writeStringField(Squad::name.name, revision.squad.name)
        gen.writeEndObject()
        gen.writeObjectFieldStart(SquadMemberHistory::member.name)
        gen.writeStringField(Member::id.name, reverseRouter.route(MemberController::class, revision.member.id))
        gen.writeStringField(Member::name.name, revision.member.name)
        gen.writeEndObject()
        gen.writeStringField(SquadMemberHistory::revisionType.name, revision.revisionType.toString())
        gen.writeEndObject()
    }
}
