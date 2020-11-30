package app.choppa.domain.iteration

import app.choppa.utils.ReverseRouter
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.beans.factory.annotation.Autowired

class IterationSerializer(
    supportedClass: Class<Iteration>? = null,
    @Autowired private val reverseRouter: ReverseRouter = ReverseRouter(),
) : StdSerializer<Iteration>(supportedClass) {

    override fun serialize(iteration: Iteration, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("id", reverseRouter.route(IterationController::class, iteration.id))
        gen.writeNumberField("number", iteration.number)
        gen.writeNumberField("startDate", iteration.startDate.toEpochMilli())
        gen.writeNumberField("endDate", iteration.endDate.toEpochMilli())
        gen.writeEndObject()
    }
}
