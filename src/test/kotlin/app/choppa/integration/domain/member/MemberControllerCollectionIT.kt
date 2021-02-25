package app.choppa.integration.domain.member

import app.choppa.domain.account.Account.Companion.UNASSIGNED_ACCOUNT
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberController
import app.choppa.domain.member.MemberService
import app.choppa.exception.EmptyListException
import app.choppa.support.factory.MemberFactory
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

@WebMvcTest(controllers = [MemberController::class])
@ActiveProfiles("test")
internal class MemberControllerCollectionIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var memberService: MemberService

    @Nested
    inner class HappyPath {

        @Test
        fun `LIST entities`() {
            val entities = MemberFactory.create(amount = 2)

            every { memberService.find(UNASSIGNED_ACCOUNT) } returns entities

            mvc.get("/api/members") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(entities)) }
            }
        }

        @Test
        fun `LIST inactive entities`() {
            val entities = listOf(Member(active = false))

            every { memberService.findInactive(UNASSIGNED_ACCOUNT) } returns entities

            mvc.get("/api/members?active=false") {
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
            val existingCollection = MemberFactory.create(amount = 3)
            val updatedCollection = existingCollection.map { Member(it.id, it.name, Chapter()) }

            every { memberService.find(existingCollection.map { it.id }, UNASSIGNED_ACCOUNT) } returns existingCollection
            every { memberService.save(updatedCollection, UNASSIGNED_ACCOUNT) } returns updatedCollection

            mvc.put("/api/members") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(updatedCollection)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/members")) }
            }
        }

        @Test
        fun `DELETE collection`() {
            val existingCollection = MemberFactory.create(amount = 3)

            every { memberService.find(existingCollection.map { it.id }, UNASSIGNED_ACCOUNT) } returns existingCollection
            every { memberService.delete(existingCollection, UNASSIGNED_ACCOUNT) } returns existingCollection

            mvc.delete("/api/members") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(existingCollection)
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `POST collection`() {
            val newCollection = MemberFactory.create(amount = 3)

            every { memberService.save(newCollection, UNASSIGNED_ACCOUNT) } returns newCollection

            mvc.post("/api/members") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(newCollection)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/members")) }
            }
        }
    }

    @Nested
    inner class SadPath {

        @Test
        fun `LIST no content`() {
            every { memberService.find(UNASSIGNED_ACCOUNT) } throws EmptyListException("No members exist yet")

            mvc.get("/api/members") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }
    }
}
