package app.choppa.integration.domain.tribe

import app.choppa.domain.rotation.RotationOptions
import app.choppa.domain.rotation.RotationService
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.TribeController
import app.choppa.domain.tribe.TribeService
import app.choppa.exception.EmptyListException
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.base.BaseControllerIT
import app.choppa.support.factory.TribeFactory
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.core.StringContains
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.util.UUID.randomUUID

@WebMvcTest(controllers = [TribeController::class])
internal class TribeControllerIT : BaseControllerIT() {
    @MockkBean
    private lateinit var tribeService: TribeService

    @MockkBean
    private lateinit var rotationService: RotationService

    private lateinit var tribe: Tribe

    @BeforeEach
    internal fun setUp() {
        tribe = TribeFactory.create()
    }

    @Nested
    @WithMockUser
    inner class HappyPath {

        @Test
        fun `LIST entities`() {
            val anotherTribe = TribeFactory.create()
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
            val updatedEntity = TribeFactory.create(tribe.id, tribe.name)

            every { tribeService.find(entity.id) } returns entity
            every {
                tribeService.save(
                    TribeFactory.create(
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

            every { tribeService.save(TribeFactory.create(tribe.id, tribe.name)) } returns newEntity

            mvc.post("/api/tribes/${newEntity.id}") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(newEntity)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/tribes/${newEntity.id}")) }
            }
        }

        @Test
        fun `POST rotation request no payload`() {
            val entity = tribe
            val options = RotationOptions(chapter = CHAPTER)

            every { rotationService.executeRotation(tribe, options) } returns entity
            every { tribeService.find(entity.id) } returns entity

            mvc.post("/api/tribes/${entity.id}:rotate") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(options)
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(entity)) }
            }
        }
    }

    @Nested
    @WithMockUser
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
            val randomUUID = randomUUID()

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
            val randomUUID = randomUUID()

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
            val randomUUID = randomUUID()

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

        @Test
        fun `POST rotation request with invalid payload`() {
            val entity = tribe

            mvc.post("/api/tribes/${entity.id}:rotate") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = "invalidPayload"
            }.andExpect {
                status { isUnprocessableEntity }
            }
        }

        @Test
        fun `POST rotation request to invalid tribe`() {
            val randomUUID = randomUUID()
            val options = RotationOptions(chapter = CHAPTER)

            every { tribeService.find(randomUUID) } throws EntityNotFoundException("Tribe with id [$randomUUID] does not exist.")

            mvc.post("/api/tribes/$randomUUID:rotate") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(options)
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `POST rotation request with no rotation options`() {
            val entity = tribe

            mvc.post("/api/tribes/${entity.id}:rotate") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isUnprocessableEntity }
            }
        }
    }
}
