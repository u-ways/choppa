package app.choppa.integration.domain.squad

import app.choppa.domain.account.Account.Companion.UNASSIGNED_ACCOUNT
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadController
import app.choppa.domain.squad.SquadService
import app.choppa.exception.EmptyListException
import app.choppa.support.factory.SquadFactory
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

@WebMvcTest(controllers = [SquadController::class])
@ActiveProfiles("test")
internal class SquadControllerCollectionIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var squadService: SquadService

    @Nested
    inner class HappyPath {

        @Test
        fun `LIST entities`() {
            val entities = SquadFactory.create(amount = 2)

            every { squadService.find(UNASSIGNED_ACCOUNT) } returns entities

            mvc.get("/api/squads") {
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
            val existingCollection = SquadFactory.create(amount = 3)
            val greenColor = "#00FF00".toRGBAInt()
            val updatedCollection = existingCollection.map { Squad(it.id, it.name, greenColor) }

            every { squadService.find(existingCollection.map { it.id }, UNASSIGNED_ACCOUNT) } returns existingCollection
            every { squadService.save(updatedCollection, UNASSIGNED_ACCOUNT) } returns updatedCollection

            mvc.put("/api/squads") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(updatedCollection)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/squads")) }
            }
        }

        @Test
        fun `DELETE collection`() {
            val existingCollection = SquadFactory.create(amount = 3)

            every { squadService.find(existingCollection.map { it.id }, UNASSIGNED_ACCOUNT) } returns existingCollection
            every { squadService.delete(existingCollection, UNASSIGNED_ACCOUNT) } returns existingCollection

            mvc.delete("/api/squads") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(existingCollection)
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `POST collection`() {
            val newCollection = SquadFactory.create(amount = 3)

            every { squadService.save(newCollection, UNASSIGNED_ACCOUNT) } returns newCollection

            mvc.post("/api/squads") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(newCollection)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/squads")) }
            }
        }
    }

    @Nested
    inner class SadPath {

        @Test
        fun `LIST no content`() {
            every { squadService.find(UNASSIGNED_ACCOUNT) } throws EmptyListException("No squads exist yet")

            mvc.get("/api/squads") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }
    }
}
