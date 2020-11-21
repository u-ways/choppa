package org.choppa.integration.domain.member

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.choppa.domain.chapter.Chapter
import org.choppa.domain.member.Member
import org.choppa.domain.member.MemberController
import org.choppa.domain.member.MemberService
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
import java.util.UUID

@WebMvcTest(controllers = [MemberController::class])
@ActiveProfiles("test")
internal class MemberControllerIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {

    @MockkBean
    private lateinit var memberService: MemberService

    private lateinit var member: Member

    @BeforeEach
    internal fun setUp() {
        member = Member()
    }

    @Nested
    inner class HappyPath {

        @Test
        fun `LIST entities`() {
            val anotherMember = Member()
            val entities = listOf(member, anotherMember)

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
        fun `GET entity by ID`() {
            val entity = member

            every { memberService.find(entity.id) } returns entity

            mvc.get("/api/members/{id}", entity.id) {
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
            val entity = member
            val updatedEntity = Member(member.id)

            every { memberService.find(entity.id) } returns entity
            every {
                memberService.save(
                    Member(
                        member.id,
                        member.name,
                        Chapter(member.chapter.id)
                    )
                )
            } returns updatedEntity

            mvc.put("/api/members/{id}", entity.id) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(updatedEntity)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/members/${entity.id}")) }
            }
        }

        @Test
        fun `DELETE entity by ID`() {
            val entity = member

            every { memberService.find(entity.id) } returns entity
            every { memberService.delete(entity) } returns entity

            mvc.delete("/api/members/{id}", entity.id) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `POST new entity`() {
            val newEntity = member

            every { memberService.save(Member(member.id, member.name, Chapter(member.chapter.id))) } returns newEntity

            mvc.post("/api/members") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(newEntity)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/members/${newEntity.id}")) }
            }
        }
    }

    @Nested
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

        @Test
        fun `GET UUID doesn't exist`() {
            val randomUUID = UUID.randomUUID()

            every { memberService.find(randomUUID) } throws EntityNotFoundException("Member with id [$randomUUID] does not exist.")

            mvc.get("/api/members/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `PUT invalid payload`() {
            val randomUUID = UUID.randomUUID()

            mvc.put("/api/members/{id}", randomUUID) {
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

            every { memberService.find(randomUUID) } throws EntityNotFoundException("Member with id [$randomUUID] does not exist.")

            mvc.delete("/api/members/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `POST invalid payload`() {
            mvc.post("/api/members") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = "invalidPayload"
            }.andExpect {
                status { isUnprocessableEntity }
            }
        }
    }
}
