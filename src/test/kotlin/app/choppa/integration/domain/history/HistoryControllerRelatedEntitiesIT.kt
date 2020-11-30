package app.choppa.integration.domain.history

import app.choppa.domain.history.History
import app.choppa.domain.history.HistoryController
import app.choppa.domain.history.HistoryService
import app.choppa.support.factory.HistoryFactory
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

@WebMvcTest(controllers = [HistoryController::class])
@ActiveProfiles("test")
internal class HistoryControllerRelatedEntitiesIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var historyService: HistoryService

    @Nested
    inner class HappyPath {
        @ParameterizedTest
        @ValueSource(strings = ["iteration", "tribe", "squad", "member"])
        fun `LIST history records by related entity id`(relatedEntity: String) {
            val id = randomUUID()
            val relatedHistory = HistoryFactory.create(2)

            every { findHistoryRelatedBy(relatedEntity, id) } returns relatedHistory

            mvc.get("/api/history?$relatedEntity=$id") {
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(relatedHistory)) }
            }
        }
    }

    private fun findHistoryRelatedBy(relatedEntity: String, id: UUID): List<History> {
        return when (relatedEntity) {
            "member" -> historyService.findRelatedByMember(id)
            "squad" -> historyService.findRelatedBySquad(id)
            "tribe" -> historyService.findRelatedByTribe(id)
            "iteration" -> historyService.findRelatedByIteration(id)
            else -> throw IllegalArgumentException("invalid related entity [$relatedEntity] for history")
        }
    }
}
