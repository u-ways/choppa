package app.choppa.acceptance.domain.history

import app.choppa.domain.history.History
import app.choppa.domain.iteration.Iteration
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class HistorySerializerTest {
    private lateinit var history: History
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        history = History(Iteration(), Tribe(), Squad(), Member())
        mapper = ObjectMapper()
    }

    @Test
    fun `Given entity DAO, when serialize, then it should return correct uniform DTO`() {
        val uniformDto = JsonPath.parse(mapper.writeValueAsString(history))

        val iterationId = uniformDto?.read<String>("$.iteration")
        val tribeId = uniformDto?.read<String>("$.tribe")
        val squadId = uniformDto?.read<String>("$.squad")
        val memberId = uniformDto?.read<String>("$.member")
        val createDate = uniformDto?.read<Long>("$.createDate")

        assertThat(iterationId, equalTo("iterations/${history.iteration.id}"))
        assertThat(tribeId, equalTo("tribes/${history.tribe.id}"))
        assertThat(squadId, equalTo("squads/${history.squad.id}"))
        assertThat(memberId, equalTo("members/${history.member.id}"))
        assertThat(createDate, equalTo(history.createDate.toEpochMilli()))
    }
}
