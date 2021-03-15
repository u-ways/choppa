package app.choppa.integration.domain.tribe

import app.choppa.domain.rotation.RotationService
import app.choppa.domain.tribe.TribeController
import app.choppa.domain.tribe.TribeService
import app.choppa.exception.EmptyListException
import app.choppa.support.base.BaseControllerIT
import app.choppa.support.factory.TribeFactory
import app.choppa.utils.Color.Companion.toRGBAInt
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.core.StringContains
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

@WebMvcTest(controllers = [TribeController::class])
internal class TribeControllerCollectionIT : BaseControllerIT() {
    @MockkBean
    private lateinit var tribeService: TribeService

    @MockkBean
    private lateinit var rotationService: RotationService

    @Nested
    @WithMockUser
    inner class HappyPath {

        @Test
        fun `LIST entities`() {
            val entities = TribeFactory.create(amount = 2)

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
        fun `PUT collection`() {
            val existingCollection = TribeFactory.create(amount = 3)
            val greenColor = "#00FF00".toRGBAInt()
            val updatedCollection = existingCollection.map { TribeFactory.create(it.id, it.name, greenColor) }

            every { tribeService.find(existingCollection.map { it.id }) } returns existingCollection
            every { tribeService.save(updatedCollection) } returns updatedCollection

            mvc.put("/api/tribes") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(updatedCollection)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/tribes")) }
            }
        }

        @Test
        fun `DELETE collection`() {
            val existingCollection = TribeFactory.create(amount = 3)

            every { tribeService.find(existingCollection.map { it.id }) } returns existingCollection
            every { tribeService.delete(existingCollection) } returns existingCollection

            mvc.delete("/api/tribes") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(existingCollection)
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `POST collection`() {
            val newCollection = TribeFactory.create(amount = 3)

            every { tribeService.save(newCollection) } returns newCollection

            mvc.post("/api/tribes") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(newCollection)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/tribes")) }
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
    }
}
