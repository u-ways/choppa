package app.choppa.integration.domain.chapter

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterController
import app.choppa.domain.chapter.ChapterService
import app.choppa.support.base.BaseControllerIT
import app.choppa.support.factory.ChapterFactory
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.get
import java.util.*
import java.util.UUID.randomUUID

@WebMvcTest(controllers = [ChapterController::class])
internal class ChapterControllerRelatedEntitiesIT : BaseControllerIT() {
    @MockkBean
    private lateinit var chapterService: ChapterService

    @Nested
    @WithMockUser
    inner class HappyPath {
        @ParameterizedTest
        @ValueSource(strings = ["squad", "tribe"])
        fun `LIST chapters by related entity id`(relatedEntity: String) {
            val id = randomUUID()
            val relatedChapters = ChapterFactory.create(2)

            every { findChaptersRelatedBy(relatedEntity, id) } returns relatedChapters

            mvc.get("/api/chapters?$relatedEntity=$id") {
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(relatedChapters)) }
            }
        }
    }

    private fun findChaptersRelatedBy(relatedEntity: String, id: UUID): List<Chapter> {
        return when (relatedEntity) {
            "squad" -> chapterService.findRelatedBySquad(id)
            "tribe" -> chapterService.findRelatedByTribe(id)
            else -> throw IllegalArgumentException("invalid related entity [$relatedEntity] for chapter")
        }
    }
}
