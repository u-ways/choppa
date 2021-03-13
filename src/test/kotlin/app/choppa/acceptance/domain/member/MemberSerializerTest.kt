package app.choppa.acceptance.domain.member

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MemberSerializerTest {
    private lateinit var member: Member
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        member = Member()
        mapper = ObjectMapper()
    }

    @Test
    fun `Given entity DAO, when serialize, then it should return correct uniform DTO`() {
        val uniformDto = JsonPath.parse(mapper.writeValueAsString(member))

        val id = uniformDto?.read<String>("$.id")
        val name = uniformDto?.read<String>("$.name")
        val active = uniformDto?.read<Boolean>("$.active")
        val squads = uniformDto?.read<String>("$.squads")

        assertThat(id, equalTo("members/${member.id}"))
        assertThat(name, equalTo(member.name))
        assertThat(active, equalTo(true))
        assertThat(squads, equalTo("squads?member=${member.id}"))
    }

    @Test
    internal fun `Given DAO with chapter, when serialize, then it should return DTO with chapter`() {
        val member = Member(chapter = Chapter())

        val uniformDto = JsonPath.parse(mapper.writeValueAsString(member))
        val chapter = uniformDto?.read<Chapter>("$.chapter")!!

        assertThat(chapter.id, equalTo(member.chapter.id))
        assertThat(chapter.name, equalTo(member.chapter.name))
        assertThat(chapter.color, equalTo(member.chapter.color))
    }
}
