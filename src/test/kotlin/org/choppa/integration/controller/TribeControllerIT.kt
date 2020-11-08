package org.choppa.integration.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.choppa.controller.TribeController
import org.choppa.exception.EmptyListException
import org.choppa.exception.EntityNotFoundException
import org.choppa.model.tribe.Tribe
import org.choppa.service.TribeService
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
import java.util.UUID

@WebMvcTest(controllers = [TribeController::class])
@ActiveProfiles("test")
internal class TribeControllerIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {

    @MockkBean
    private lateinit var tribeService: TribeService

    private lateinit var tribe: Tribe

    @BeforeEach
    internal fun setUp() {
        tribe = Tribe()
    }

    @Nested
    inner class HappyPath {

        @Test
        fun `LIST entities`() {
            val anotherTribe = Tribe()
            val entities = listOf(tribe, anotherTribe)

            every { tribeService.find() } returns entities

            mvc.get("/api/tribes") {
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
            val entity = tribe

            every { tribeService.find(entity.id) } returns entity

            mvc.get("/api/tribes/{id}", entity.id) {
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
            val entity = tribe
            val updatedEntity = Tribe(tribe.id, tribe.name)

            every { tribeService.find(entity.id) } returns entity
            every {
                tribeService.save(
                    Tribe(
                        tribe.id,
                        tribe.name
                    )
                )
            } returns updatedEntity

            mvc.put("/api/tribes/{id}", entity.id) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(updatedEntity)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/tribes/${entity.id}")) }
            }
        }

        @Test
        fun `DELETE entity by ID`() {
            val entity = tribe

            every { tribeService.find(entity.id) } returns entity
            every { tribeService.delete(entity) } returns entity

            mvc.delete("/api/tribes/{id}", entity.id) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `POST new entity`() {
            val newEntity = tribe

            every { tribeService.save(Tribe(tribe.id, tribe.name)) } returns newEntity

            mvc.post("/api/tribes") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(newEntity)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/tribes/${newEntity.id}")) }
            }
        }
    }

    @Nested
    inner class SadPath {

        @Test
        fun `LIST no content`() {
            every { tribeService.find() } throws EmptyListException("No tribes exist yet")

            mvc.get("/api/tribes") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `GET UUID doesn't exist`() {
            val randomUUID = UUID.randomUUID()

            every { tribeService.find(randomUUID) } throws EntityNotFoundException("Tribe with id [$randomUUID] does not exist.")

            mvc.get("/api/tribes/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `PUT invalid payload`() {
            val randomUUID = UUID.randomUUID()

            mvc.put("/api/tribes/{id}", randomUUID) {
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

            every { tribeService.find(randomUUID) } throws EntityNotFoundException("Tribe with id [$randomUUID] does not exist.")

            mvc.delete("/api/tribes/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `POST invalid payload`() {
            mvc.post("/api/tribes") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = "invalidPayload"
            }.andExpect {
                status { isUnprocessableEntity }
            }
        }
    }
}
