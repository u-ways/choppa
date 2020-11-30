package app.choppa.integration.domain.history

import app.choppa.domain.history.History
import app.choppa.domain.history.HistoryController
import app.choppa.domain.history.HistoryService
import app.choppa.exception.EmptyListException
import app.choppa.support.factory.HistoryFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.Instant.now
import java.time.Instant.ofEpochMilli

@WebMvcTest(controllers = [HistoryController::class])
@ActiveProfiles("test")
internal class HistoryControllerIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {

    @MockkBean
    private lateinit var historyService: HistoryService

    private lateinit var history: History

    @BeforeEach
    internal fun setUp() {
        history = HistoryFactory.create()
    }

    @Nested
    inner class HappyPath {
        @Test
        fun `LIST entities`() {
            val entities = HistoryFactory.create(2)

            every { historyService.find() } returns entities

            mvc.get("/api/history") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(entities)) }
            }
        }

        @ParameterizedTest
        @ValueSource(strings = ["before", "after"])
        fun `LIST entities by date filter`(filter: String) {
            val entities = HistoryFactory.create(2)
            val createDate = now().toEpochMilli()

            every { findHistoryBy(filter, createDate) } returns entities

            mvc.get("/api/history?$filter=$createDate") {
                contentType = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(entities)) }
            }
        }
    }

    @Nested
    inner class SadPath {
        @Test
        fun `LIST no content`() {
            every { historyService.find() } throws EmptyListException("No history records exist yet")

            mvc.get("/api/history") {
                contentType = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }
    }

    private fun findHistoryBy(dateFilter: String, createDate: Long): List<History> {
        return when (dateFilter) {
            "before" -> historyService.findAllByCreateDateBefore(ofEpochMilli(createDate))
            "after" -> historyService.findAllByCreateDateAfter(ofEpochMilli(createDate))
            else -> throw IllegalArgumentException("invalid date filter [$dateFilter] for history")
        }
    }
}
