package app.choppa.domain.member

import app.choppa.domain.base.BaseSerializer
import app.choppa.domain.base.BaseSerializer.QueryType.ITERATIONS
import app.choppa.domain.base.BaseSerializer.QueryType.SQUADS
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider

class MemberSerializer(
    supportedClass: Class<Member>? = null
) : BaseSerializer<Member>(supportedClass) {
    override fun serialize(member: Member, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField(member::id.name, reverseRouter.route(MemberController::class, member.id))
        gen.writeStringField(member::name.name, member.name)
        gen.writeObjectField(member::chapter.name, member.chapter)
        gen.writeQueryField(SQUADS, member)
        gen.writeQueryField(ITERATIONS, member)
        gen.writeEndObject()
    }
}
