package app.choppa.acceptance.domain.squad

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.support.factory.SquadFactory
import app.choppa.support.matchers.containsInAnyOrder
import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SquadSerializerTest {
    private val green = 1655133696
    private lateinit var squad: Squad
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        squad = Squad(color = green)
        mapper = ObjectMapper()
    }

    @Test
    fun `Given entity DAO, when serialize, then it should return correct uniform DTO`() {
        val uniformDto = JsonPath.parse(mapper.writeValueAsString(squad))

        val id = uniformDto?.read<String>("$.id")
        val name = uniformDto?.read<String>("$.name")
        val color = uniformDto?.read<String>("$.color")
        val tribe = uniformDto?.read<String>("$.tribe")
        val chapters = uniformDto?.read<String>("$.chapters")
        val members = uniformDto?.read<List<Member>>("$.members")
        val iterations = uniformDto?.read<String>("$.iterations")
        val history = uniformDto?.read<String>("$.history")

        assertThat(id, equalTo("squads/${squad.id}"))
        assertThat(name, equalTo(squad.name))
        assertThat(color, equalTo("#62a75600"))
        assertThat(tribe, equalTo("tribes/${squad.tribe.id}"))
        assertThat(chapters, equalTo("chapters?squad=${squad.id}"))
        assertThat(members, equalTo(emptyList()))
        assertThat(iterations, equalTo("iterations?squad=${squad.id}"))
        assertThat(history, equalTo("history?squad=${squad.id}"))
    }

    @Test
    internal fun `Given DAO with members, when serialize, then it should return DTO with members`() {
        val squad = SquadFactory.create(membersAmount = 2)

        val uniformDto = JsonPath.parse(mapper.writeValueAsString(squad))
        val members = uniformDto?.read<List<Member>>("$.members")

        assertThat(members!!, List<Member>::containsInAnyOrder, squad.members)
    }
}
