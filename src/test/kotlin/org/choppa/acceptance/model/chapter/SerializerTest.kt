package org.choppa.acceptance.model.chapter

import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import org.choppa.model.chapter.Chapter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

internal class SerializerTest {
    private val chapter = Chapter(id = UUID.randomUUID(), name = "chapterName")
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        mapper = ObjectMapper()
    }

    @Test
    fun `Given entity DAO, when serialize, then it should return correct uniform DTO`() {
        val uniformDto = JsonPath.parse(mapper.writeValueAsString(chapter))

        val id = uniformDto?.read<String>("$.id")
        val name = uniformDto?.read<String>("$.name")
        val members = uniformDto?.read<String>("$.members")

        assertThat(id, equalTo("chapters/${chapter.id}"))
        assertThat(name, equalTo(chapter.name))
        assertThat(members, equalTo("members?chapter=${chapter.id}"))
    }
}
