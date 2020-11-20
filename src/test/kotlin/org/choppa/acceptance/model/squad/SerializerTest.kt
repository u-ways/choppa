package org.choppa.acceptance.model.squad

import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import org.choppa.model.member.Member
import org.choppa.model.squad.Squad
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SerializerTest {
    private lateinit var squad: Squad
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        squad = Squad()
        mapper = ObjectMapper()
    }

    @Test
    fun `Given entity DAO, when serialize, then it should return correct uniform DTO`() {
        val uniformDto = JsonPath.parse(mapper.writeValueAsString(squad))

        val id = uniformDto?.read<String>("$.id")
        val name = uniformDto?.read<String>("$.name")
        val tribe = uniformDto?.read<String>("$.tribe")
        val members = uniformDto?.read<List<String>>("$.members")
        val iterations = uniformDto?.read<String>("$.iterations")
        val history = uniformDto?.read<String>("$.history")

        assertThat(id, equalTo("squads/${squad.id}"))
        assertThat(name, equalTo(squad.name))
        assertThat(tribe, equalTo("tribes/${squad.tribe.id}"))
        assertThat(members, equalTo(emptyList()))
        assertThat(iterations, equalTo("iterations?squad=${squad.id}"))
        assertThat(history, equalTo("history?squad=${squad.id}"))
    }

    @Test
    fun `Given entity DAO with members, when serialize, then it should return correct uniform DTO`() {
        val squadWithMembers = Squad(members = mutableListOf(Member()))
        val uniformDto = JsonPath.parse(mapper.writeValueAsString(squadWithMembers))
        val members = uniformDto?.read<List<String>>("$.members")
        assertThat(members?.first(), equalTo("members/${squadWithMembers.members.first().id}"))
    }
}
