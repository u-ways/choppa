package org.choppa.domain.member

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import org.choppa.domain.base.BaseSerializer
import org.choppa.domain.base.BaseSerializer.QueryType.HISTORY
import org.choppa.domain.base.BaseSerializer.QueryType.ITERATIONS
import org.choppa.domain.base.BaseSerializer.QueryType.SQUADS
import org.choppa.domain.chapter.ChapterController

class MemberSerializer(
    supportedClass: Class<Member>? = null
) : BaseSerializer<Member>(supportedClass) {
    override fun serialize(member: Member, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("id", reverseRouter.route(MemberController::class, member.id))
        gen.writeStringField("name", member.name)
        gen.writeStringField("chapter", reverseRouter.route(ChapterController::class, member.chapter.id))
        gen.queryComponent(SQUADS, member)
        gen.queryComponent(ITERATIONS, member)
        gen.queryComponent(HISTORY, member)
        gen.writeEndObject()
    }
}
