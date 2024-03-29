package app.choppa.integration.domain.chapter

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterController
import app.choppa.domain.chapter.ChapterService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.base.BaseControllerIT
import app.choppa.support.factory.ChapterFactory
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.core.StringContains
import org.junit.jupiter.api.BeforeEach
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
import java.util.UUID.randomUUID

@WebMvcTest(controllers = [ChapterController::class])
internal class ChapterControllerIT : BaseControllerIT() {

    @MockkBean
    private lateinit var chapterService: ChapterService

    private lateinit var chapter: Chapter

    @BeforeEach
    internal fun setUp() {
        chapter = ChapterFactory.create()
    }

    @Nested
    @WithMockUser
    inner class HappyPath {
        @Test
        fun `GET entity by ID`() {
            val entity = chapter

            every { chapterService.find(entity.id) } returns entity

            mvc.get("/api/chapters/{id}", entity.id) {
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
            val entity = chapter
            val updatedEntity = ChapterFactory.create(entity.id)

            every { chapterService.find(entity.id) } returns entity
            every { chapterService.save(updatedEntity) } returns updatedEntity

            mvc.put("/api/chapters/{id}", entity.id) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(updatedEntity)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/chapters/${entity.id}")) }
            }
        }

        @Test
        fun `DELETE entity by ID`() {
            val entity = chapter

            every { chapterService.find(entity.id) } returns entity
            every { chapterService.delete(entity) } returns entity

            mvc.delete("/api/chapters/{id}", entity.id) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `POST new entity`() {
            val newEntity = chapter

            every { chapterService.save(newEntity) } returns newEntity

            mvc.post("/api/chapters/${newEntity.id}") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(newEntity)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/chapters/${newEntity.id}")) }
            }
        }
    }

    @Nested
    @WithMockUser
    inner class SadPath {
        @Test
        fun `GET UUID doesn't exist`() {
            val randomUUID = randomUUID()

            every {
                chapterService.find(
                    randomUUID
                )
            } throws EntityNotFoundException("Chapter with id [$randomUUID] does not exist.")

            mvc.get("/api/chapters/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `PUT invalid payload`() {
            val randomUUID = randomUUID()

            mvc.put("/api/chapters/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = "invalidPayload"
            }.andExpect {
                status { isUnprocessableEntity }
            }
        }

        @Test
        fun `DELETE UUID doesn't exist`() {
            val randomUUID = randomUUID()

            every {
                chapterService.find(
                    randomUUID
                )
            } throws EntityNotFoundException("Chapter with id [$randomUUID] does not exist.")

            mvc.delete("/api/chapters/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `POST invalid payload`() {
            mvc.post("/api/chapters/${chapter.id}") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = "invalidPayload"
            }.andExpect {
                status { isUnprocessableEntity }
            }
        }

        @Test
        fun `POST invalid color payload`() {
            mvc.post("/api/chapters/${chapter.id}") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content =
                    """
                        {
                            "id": "squads/${chapter.id}",
                            "name": "${chapter.name}",
                            "color": "#123456789"
                        }
                    """.trimIndent()
            }.andExpect {
                status { isUnprocessableEntity }
            }
        }
    }
}
