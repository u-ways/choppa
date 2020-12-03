package app.choppa.acceptance.domain.member

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
        val members = uniformDto?.read<String>("$.chapter")
        val squads = uniformDto?.read<String>("$.squads")
        val iterations = uniformDto?.read<String>("$.iterations")
        val history = uniformDto?.read<String>("$.history")

        assertThat(id, equalTo("members/${member.id}"))
        assertThat(name, equalTo(member.name))
        assertThat(members, equalTo("chapters/${member.chapter.id}"))
        assertThat(squads, equalTo("squads?member=${member.id}"))
        assertThat(iterations, equalTo("iterations?member=${member.id}"))
        assertThat(history, equalTo("history?member=${member.id}"))
    }
}
