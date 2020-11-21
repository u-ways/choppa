package org.choppa.acceptance.model.chapter

import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.sameInstance
import org.choppa.exception.UnprocessableEntityException
import org.choppa.model.chapter.Chapter
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DeserializerTest {
    private val white = -256
    private lateinit var chapter: Chapter
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        chapter = Chapter(color = white)
        mapper = ObjectMapper()
    }

    @Test
    fun `Given valid entity DTO, when deserialize, then it should return correct DAO`() {
        val validDto =
            """
            {
                "id": "chapters/${chapter.id}",
                "name": "${chapter.name}",
                "members": "members?chapter=${chapter.id}",
                "color": "#ffffff"
            }
            """.trimIndent()

        val dao = mapper.readValue(validDto, Chapter::class.java)

        assertThat(dao.id, equalTo(chapter.id))
        assertThat(dao.name, equalTo(chapter.name))
        assertThat(dao.color, equalTo(chapter.color))
    }

    @Test
    fun `Given valid uniform DTO, when deserialize, then it should return correct DAO type`() {
        val validDto = "\"chapters/${chapter.id}\""
        val dao = mapper.readValue(validDto, Chapter::class.java)

        assertThat(dao.id, equalTo(chapter.id))
        assertThat(dao::class, sameInstance(Chapter::class))
    }

    @Test
    fun `Given invalid entity DTO, when deserialize, then it should throw UnprocessableEntityException`() {
        val invalidDto = "{ invalidDto }"

        assertThrows(UnprocessableEntityException::class.java) {
            mapper.readValue(invalidDto, Chapter::class.java)
        }
    }

    @Test
    fun `Given invalid color in entity DTO, when deserialize, then it it should throw UnprocessableEntityException`() {
        val invalidDto =
            """
            {
                "id": "chapters/${chapter.id}",
                "name": "${chapter.name}",
                "color": "#ffffff0"
            }
            """.trimIndent()

        assertThrows(UnprocessableEntityException::class.java) {
            mapper.readValue(invalidDto, Chapter::class.java)
        }
    }
}
