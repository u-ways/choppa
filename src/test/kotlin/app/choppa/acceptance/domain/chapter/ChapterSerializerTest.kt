package app.choppa.acceptance.domain.chapter

import app.choppa.domain.chapter.Chapter
import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ChapterSerializerTest {
    private val blue = 65280
    private lateinit var chapter: Chapter
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        chapter = Chapter(color = blue)
        mapper = ObjectMapper()
    }

    @Test
    fun `Given entity DAO, when serialize, then it should return correct uniform DTO`() {
        val uniformDto = JsonPath.parse(mapper.writeValueAsString(chapter))

        val id = uniformDto?.read<String>("$.id")
        val name = uniformDto?.read<String>("$.name")
        val color = uniformDto?.read<String>("$.color")
        val members = uniformDto?.read<String>("$.members")

        assertThat(id, equalTo("chapters/${chapter.id}"))
        assertThat(name, equalTo(chapter.name))
        assertThat(color, equalTo("#0000ff00"))
        assertThat(members, equalTo("members?chapter=${chapter.id}"))
    }
}
