package org.choppa.model.iteration

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class Serializer(supportedClass: Class<Iteration>? = null) : StdSerializer<Iteration>(supportedClass) {
    override fun serialize(iteration: Iteration, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()

        gen.writeStringField("id", "iterations/${iteration.id}")
        gen.writeNumberField("number", iteration.number)
        gen.writeNumberField("startDate", iteration.startDate.toEpochMilli())
        gen.writeNumberField("endDate", iteration.endDate.toEpochMilli())

        gen.writeEndObject()
    }
}
