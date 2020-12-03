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
        gen.writeStringField(tribe::id.name, reverseRouter.route(TribeController::class, tribe.id))
        gen.writeStringField(tribe::name.name, tribe.name)
        gen.writeStringField(tribe::color.name, tribe.color.toRGBAHex())
        gen.writeQueryField(CHAPTERS, tribe)
        gen.writeQueryField(MEMBERS, tribe)
        gen.writeQueryField(SQUADS, tribe)
        gen.writeQueryField(ITERATIONS, tribe)
        gen.writeQueryField(HISTORY, tribe)
        gen.writeEndObject()
    }
}
