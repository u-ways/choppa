package app.choppa.domain.tribe

import app.choppa.domain.base.BaseSerializer
import app.choppa.domain.base.BaseSerializer.QueryType.CHAPTERS
import app.choppa.domain.base.BaseSerializer.QueryType.HISTORY
import app.choppa.domain.base.BaseSerializer.QueryType.ITERATIONS
import app.choppa.domain.base.BaseSerializer.QueryType.MEMBERS
import app.choppa.domain.base.BaseSerializer.QueryType.SQUADS
import app.choppa.utils.Color.Companion.toRGBAHex
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider

class TribeSerializer(
    supportedClass: Class<Tribe>? = null
) : BaseSerializer<Tribe>(supportedClass) {
    override fun serialize(tribe: Tribe, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("id", reverseRouter.route(TribeController::class, tribe.id))
        gen.writeStringField("name", tribe.name)
        gen.writeStringField("color", tribe.color.toRGBAHex())
        gen.writeQueryField(CHAPTERS, tribe)
        gen.writeQueryField(MEMBERS, tribe)
        gen.writeQueryField(SQUADS, tribe)
        gen.writeQueryField(ITERATIONS, tribe)
        gen.writeQueryField(HISTORY, tribe)
        gen.writeEndObject()
    }
}
