package app.choppa.integration.domain.iteration

import app.choppa.domain.iteration.Iteration
import app.choppa.domain.iteration.IterationController
import app.choppa.domain.iteration.IterationService
import app.choppa.exception.EmptyListException
import app.choppa.support.factory.IterationFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.core.StringContains
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.Instant.now

@WebMvcTest(controllers = [IterationController::class])
@ActiveProfiles("test")
internal class IterationControllerCollectionIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {

    @MockkBean
    private lateinit var iterationService: IterationService

    @Nested
    inner class HappyPath {

        @Test
        fun `LIST entities`() {
            val entities = IterationFactory.create(amount = 3)

            every { iterationService.find() } returns entities

            mvc.get("/api/iterations") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(entities)) }
            }
        }

        @Test
        fun `PUT collection`() {
            val existingCollection = IterationFactory.create(amount = 3)
            val updatedCollection = existingCollection.map { Iteration(it.id, it.number, now()) }

            every { iterationService.find(existingCollection.map { it.id }) } returns existingCollection
            every { iterationService.save(updatedCollection) } returns updatedCollection

            mvc.put("/api/iterations") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(updatedCollection)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/iterations")) }
            }
        }

        @Test
        fun `DELETE entity by ID`() {
            val existingCollection = IterationFactory.create(amount = 3)

            every { iterationService.find(existingCollection.map { it.id }) } returns existingCollection
            every { iterationService.delete(existingCollection) } returns existingCollection

            mvc.delete("/api/iterations") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(existingCollection)
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `POST new entity`() {
            val newCollection = IterationFactory.create(amount = 3)

            every { iterationService.save(newCollection) } returns newCollection

            mvc.post("/api/iterations") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(newCollection)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/iterations")) }
            }
        }
    }

    @Nested
    inner class SadPath {

        @Test
        fun `LIST no content`() {
            every { iterationService.find() } throws EmptyListException("No iterations exist yet")

            mvc.get("/api/iterations") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }
    }
}
