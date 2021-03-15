package app.choppa.integration.domain.member

import app.choppa.domain.member.MemberController
import app.choppa.domain.member.MemberService
import app.choppa.exception.EmptyListException
import app.choppa.support.base.BaseControllerIT
import app.choppa.support.factory.ChapterFactory
import app.choppa.support.factory.MemberFactory
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

@WebMvcTest(controllers = [MemberController::class])
internal class MemberControllerCollectionIT : BaseControllerIT() {
    @MockkBean
    private lateinit var memberService: MemberService

    @Nested
    @WithMockUser
    inner class HappyPath {

        @Test
        fun `LIST entities`() {
            val entities = MemberFactory.create(amount = 2)

            every { memberService.find() } returns entities

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
            val entities = listOf(MemberFactory.create(active = false))

            every { memberService.findInactive() } returns entities

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
            val updatedCollection =
                existingCollection.map { MemberFactory.create(it.id, it.name, ChapterFactory.create()) }

            every { memberService.find(existingCollection.map { it.id }) } returns existingCollection
            every { memberService.save(updatedCollection) } returns updatedCollection

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

            every { memberService.find(existingCollection.map { it.id }) } returns existingCollection
            every { memberService.delete(existingCollection) } returns existingCollection

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

            every { memberService.save(newCollection) } returns newCollection

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
    @WithMockUser
    inner class SadPath {

        @Test
        fun `LIST no content`() {
            every { memberService.find() } throws EmptyListException("No members exist yet")

            mvc.get("/api/members") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }
    }
}
