package app.choppa.integration.domain.tribe

import app.choppa.domain.account.Account.Companion.UNASSIGNED_ACCOUNT
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.TribeController
import app.choppa.domain.tribe.TribeService
import app.choppa.exception.EmptyListException
import app.choppa.support.factory.TribeFactory
import app.choppa.utils.Color.Companion.toRGBAInt
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
import org.springframework.test.web.servlet.*

@WebMvcTest(controllers = [TribeController::class])
@ActiveProfiles("test")
internal class TribeControllerCollectionIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var tribeService: TribeService

    @Nested
    inner class HappyPath {

        @Test
        fun `LIST entities`() {
            val entities = TribeFactory.create(amount = 2)

            every { tribeService.find(UNASSIGNED_ACCOUNT) } returns entities

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
            val updatedCollection = existingCollection.map { Tribe(it.id, it.name, greenColor) }

            every { tribeService.find(existingCollection.map { it.id }, UNASSIGNED_ACCOUNT) } returns existingCollection
            every { tribeService.save(updatedCollection, UNASSIGNED_ACCOUNT) } returns updatedCollection

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

            every { tribeService.find(existingCollection.map { it.id }, UNASSIGNED_ACCOUNT) } returns existingCollection
            every { tribeService.delete(existingCollection, UNASSIGNED_ACCOUNT) } returns existingCollection

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

            every { tribeService.save(newCollection, UNASSIGNED_ACCOUNT) } returns newCollection

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
    inner class SadPath {

        @Test
        fun `LIST no content`() {
            every { tribeService.find(UNASSIGNED_ACCOUNT) } throws EmptyListException("No tribes exist yet")

            mvc.get("/api/tribes") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }
    }
}
