package app.choppa.integration.domain.member

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberController
import app.choppa.domain.member.MemberService
import app.choppa.exception.EntityNotFoundException
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
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

            mvc.post("/api/members/${newEntity.id}") {
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
