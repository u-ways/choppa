package app.choppa.integration.domain.chapter

import app.choppa.domain.chapter.ChapterController
import app.choppa.domain.chapter.ChapterService
import app.choppa.exception.EmptyListException
import app.choppa.support.base.BaseControllerIT
import app.choppa.support.factory.ChapterFactory
import app.choppa.utils.Color.Companion.toRGBAInt
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

@WebMvcTest(controllers = [ChapterController::class])
internal class ChapterControllerCollectionIT : BaseControllerIT() {
    @MockkBean
    private lateinit var chapterService: ChapterService

    @Nested
    @WithMockUser
    inner class HappyPath {

        @Test
        fun `LIST entities`() {
            val entities = ChapterFactory.create(amount = 2)

            every { chapterService.find() } returns entities

            mvc.get("/api/chapters") {
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
            val existingCollection = ChapterFactory.create(amount = 3)
            val greenColor = "#00FF00".toRGBAInt()
            val updatedCollection = existingCollection.map { ChapterFactory.create(it.id, it.name, greenColor) }

            every { chapterService.find(existingCollection.map { it.id }) } returns existingCollection
            every { chapterService.save(updatedCollection) } returns updatedCollection

            mvc.put("/api/chapters") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(updatedCollection)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/chapters")) }
            }
        }

        @Test
        fun `DELETE collection`() {
            val existingCollection = ChapterFactory.create(amount = 3)

            every { chapterService.find(existingCollection.map { it.id }) } returns existingCollection
            every { chapterService.delete(existingCollection) } returns existingCollection

            mvc.delete("/api/chapters") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(existingCollection)
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `POST collection`() {
            val newCollection = ChapterFactory.create(amount = 3)

            every { chapterService.save(newCollection) } returns newCollection

            mvc.post("/api/chapters") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(newCollection)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/chapters")) }
            }
        }
    }

    @Nested
    @WithMockUser
    inner class SadPath {

        @Test
        fun `LIST no content`() {
            every { chapterService.find() } throws EmptyListException("No chapters exist yet")

            mvc.get("/api/chapters") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }
    }
}
