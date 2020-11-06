package org.choppa.acceptance.model.chapter

import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.choppa.helpers.exception.UnprocessableEntityException
import org.choppa.model.chapter.Chapter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DeserializerTest {
    private lateinit var chapter: Chapter
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        chapter = Chapter()
        mapper = ObjectMapper()
    }

    @Test
    fun `Given valid entity DTO, when deserialize, then it should return correct DAO`() {
        val validDto =
            """
            {
                "id": "chapters/${chapter.id}",
                "name": "${chapter.name}",
                "members": "members?chapter=${chapter.id}"
            }
            """.trimIndent()

        val dao = mapper.readValue(validDto, Chapter::class.java)

        assertThat(dao.id, equalTo(chapter.id))
        assertThat(dao.name, equalTo(chapter.name))
    }

    @Test
    fun `Given invalid entity DTO, when deserialize, then it should throw UnprocessableEntityException`() {
        val invalidDto = "{ invalidDto }"

        Assertions.assertThrows(UnprocessableEntityException::class.java) {
            mapper.readValue(invalidDto, Chapter::class.java)
        }
    }
}
