package app.choppa.integration.domain.member

import app.choppa.domain.account.AccountService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberController
import app.choppa.domain.member.MemberService
import app.choppa.support.factory.MemberFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.*
import java.util.UUID.randomUUID

@WebMvcTest(controllers = [MemberController::class])
@ActiveProfiles("test")
internal class MemberControllerRelatedEntitiesIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var memberService: MemberService

    @MockkBean(relaxed = true)
    private lateinit var accountService: AccountService

    @Nested
    @WithMockUser
    inner class HappyPath {
        @ParameterizedTest
        @ValueSource(strings = ["chapter", "squad", "tribe"])
        fun `LIST members by related entity id`(relatedEntity: String) {
            val id = randomUUID()
            val relatedMembers = MemberFactory.create(2)

            every { findMembersRelatedBy(relatedEntity, id) } returns relatedMembers

            mvc.get("/api/members?$relatedEntity=$id") {
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(relatedMembers)) }
            }
        }
    }

    private fun findMembersRelatedBy(relatedEntity: String, id: UUID): List<Member> {
        return when (relatedEntity) {
            "chapter" -> memberService.findRelatedByChapter(id)
            "squad" -> memberService.findRelatedBySquad(id)
            "tribe" -> memberService.findRelatedByTribe(id)
            else -> throw IllegalArgumentException("invalid related entity [$relatedEntity] for member")
        }
    }
}
