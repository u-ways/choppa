package app.choppa.integration.domain.iteration

import app.choppa.domain.iteration.Iteration
import app.choppa.domain.iteration.IterationController
import app.choppa.domain.iteration.IterationService
import app.choppa.support.factory.IterationFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.UUID
import java.util.UUID.randomUUID

@WebMvcTest(controllers = [IterationController::class])
@ActiveProfiles("test")
internal class IterationControllerRelatedEntitiesIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var iterationService: IterationService

    @Nested
    inner class HappyPath {
        @ParameterizedTest
        @ValueSource(strings = ["tribe", "squad", "member"])
        fun `LIST iterations records by related entity id`(relatedEntity: String) {
            val id = randomUUID()
            val relatedHistory = IterationFactory.create(2)

            every { findIterationsRelatedBy(relatedEntity, id) } returns relatedHistory

            mvc.get("/api/iterations?$relatedEntity=$id") {
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(relatedHistory)) }
            }
        }
    }

    private fun findIterationsRelatedBy(relatedEntity: String, id: UUID): List<Iteration> {
        return when (relatedEntity) {
            "member" -> iterationService.findRelatedByMember(id)
            "squad" -> iterationService.findRelatedBySquad(id)
            "tribe" -> iterationService.findRelatedByTribe(id)
            else -> throw IllegalArgumentException("invalid related entity [$relatedEntity] for iteration")
        }
    }
}
