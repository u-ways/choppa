package app.choppa.acceptance.domain.tribe

import app.choppa.domain.tribe.Tribe
import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TribeSerializerTest {
    private val red = -16777216
    private lateinit var tribe: Tribe
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        tribe = Tribe(color = red)
        mapper = ObjectMapper()
    }

    @Test
    fun `Given entity DAO, when serialize, then it should return correct uniform DTO`() {
        val uniformDto = JsonPath.parse(mapper.writeValueAsString(tribe))

        val id = uniformDto?.read<String>("$.id")
        val name = uniformDto?.read<String>("$.name")
        val color = uniformDto?.read<String>("$.color")
        val chapters = uniformDto?.read<String>("$.chapters")
        val members = uniformDto?.read<String>("$.members")
        val squads = uniformDto?.read<String>("$.squads")

        assertThat(id, equalTo("tribes/${tribe.id}"))
        assertThat(name, equalTo(tribe.name))
        assertThat(color, equalTo("#ff000000"))
        assertThat(chapters, equalTo("chapters?tribe=${tribe.id}"))
        assertThat(members, equalTo("members?tribe=${tribe.id}"))
        assertThat(squads, equalTo("squads?tribe=${tribe.id}"))
    }
}
