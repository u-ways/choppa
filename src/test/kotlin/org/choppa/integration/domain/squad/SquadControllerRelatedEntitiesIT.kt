package org.choppa.integration.domain.squad

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.choppa.domain.squad.Squad
import org.choppa.domain.squad.SquadController
import org.choppa.domain.squad.SquadService
import org.choppa.support.factory.SquadFactory
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.UUID
import java.util.UUID.randomUUID

@WebMvcTest(controllers = [SquadController::class])
@ActiveProfiles("test")
internal class SquadControllerRelatedEntitiesIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var squadService: SquadService

    @Nested
    inner class HappyPath {
        @ParameterizedTest
        @ValueSource(strings = ["member", "tribe"])
        fun `LIST squads by related entity id`(relatedEntity: String) {
            val id = randomUUID()
            val relatedSquads = SquadFactory.create(amount = 2)

            every { findSquadsRelatedBy(relatedEntity, id) } returns relatedSquads

            mvc.get("/api/squads?$relatedEntity=$id") {
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(relatedSquads)) }
            }
        }
    }

    private fun findSquadsRelatedBy(relatedEntity: String, id: UUID): List<Squad> {
        return when (relatedEntity) {
            "member" -> squadService.findRelatedByMember(id)
            "tribe" -> squadService.findRelatedByTribe(id)
            else -> throw IllegalArgumentException("invalid related entity [$relatedEntity] for squad")
        }
    }
}
