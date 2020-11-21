package org.choppa.integration.domain.iteration

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.choppa.domain.iteration.Iteration
import org.choppa.domain.iteration.IterationController
import org.choppa.domain.iteration.IterationService
import org.choppa.exception.EmptyListException
import org.choppa.exception.EntityNotFoundException
import org.hamcrest.core.StringContains
import org.junit.jupiter.api.BeforeEach
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
import java.time.Instant.ofEpochMilli
import java.util.UUID

@WebMvcTest(controllers = [IterationController::class])
@ActiveProfiles("test")
internal class IterationControllerIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {

    @MockkBean
    private lateinit var iterationService: IterationService

    private lateinit var iteration: Iteration

    @BeforeEach
    internal fun setUp() {
        iteration = Iteration()
    }

    @Nested
    inner class HappyPath {

        @Test
        fun `LIST entities`() {
            val anotherIteration = Iteration()
            val entities = listOf(iteration, anotherIteration)

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
        fun `GET entity by ID`() {
            val entity = iteration

            every { iterationService.find(entity.id) } returns entity

            mvc.get("/api/iterations/{id}", entity.id) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(entity)) }
            }
        }

        @Test
        fun `PUT entity by ID`() {
            val entity = iteration
            val updatedEntity = Iteration(iteration.id)

            every { iterationService.find(entity.id) } returns entity
            every {
                iterationService.save(
                    Iteration(
                        updatedEntity.id,
                        updatedEntity.number,
                        ofEpochMilli(updatedEntity.startDate.toEpochMilli()),
                        ofEpochMilli(updatedEntity.endDate.toEpochMilli())
                    )
                )
            } returns updatedEntity

            mvc.put("/api/iterations/{id}", entity.id) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(updatedEntity)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/iterations/${entity.id}")) }
            }
        }

        @Test
        fun `DELETE entity by ID`() {
            val entity = iteration

            every { iterationService.find(entity.id) } returns entity
            every { iterationService.delete(entity) } returns entity

            mvc.delete("/api/iterations/{id}", entity.id) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `POST new entity`() {
            val newEntity = iteration

            every {
                iterationService.save(
                    Iteration(
                        newEntity.id,
                        newEntity.number,
                        ofEpochMilli(newEntity.startDate.toEpochMilli()),
                        ofEpochMilli(newEntity.endDate.toEpochMilli())
                    )
                )
            } returns newEntity

            mvc.post("/api/iterations") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(newEntity)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/iterations/${newEntity.id}")) }
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

        @Test
        fun `GET UUID doesn't exist`() {
            val randomUUID = UUID.randomUUID()

            every { iterationService.find(randomUUID) } throws EntityNotFoundException("Iteration with id [$randomUUID] does not exist.")

            mvc.get("/api/iterations/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `PUT invalid payload`() {
            val randomUUID = UUID.randomUUID()

            mvc.put("/api/iterations/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = "invalidPayload"
            }.andExpect {
                status { isUnprocessableEntity }
            }
        }

        @Test
        fun `DELETE UUID doesn't exist`() {
            val randomUUID = UUID.randomUUID()

            every { iterationService.find(randomUUID) } throws EntityNotFoundException("Iteration with id [$randomUUID] does not exist.")

            mvc.delete("/api/iterations/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `POST invalid payload`() {
            mvc.post("/api/iterations") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = "invalidPayload"
            }.andExpect {
                status { isUnprocessableEntity }
            }
        }
    }
}
