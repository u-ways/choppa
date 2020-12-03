package app.choppa.domain.iteration

import app.choppa.domain.base.BaseSerializer
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider

class IterationSerializer(
    supportedClass: Class<Iteration>? = null
) : BaseSerializer<Iteration>(supportedClass) {
    override fun serialize(iteration: Iteration, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField(iteration::id.name, reverseRouter.route(IterationController::class, iteration.id))
        gen.writeNumberField(iteration::number.name, iteration.number)
        gen.writeNumberField(iteration::startDate.name, iteration.startDate.toEpochMilli())
        gen.writeNumberField(iteration::endDate.name, iteration.endDate.toEpochMilli())
        gen.writeEndObject()
    }
}
