package app.choppa.acceptance.domain.squad.history

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.history.SquadMemberHistory
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.jupiter.api.Test

class SquadMemberHistoryTest {
    @Test
    fun `verify the equals and hashCode contract`() {
        EqualsVerifier
            .configure().suppress(Warning.SURROGATE_KEY)
            .forClass(SquadMemberHistory::class.java)
            .withPrefabValues(Member::class.java, Member(), Member())
            .withPrefabValues(Squad::class.java, Squad(), Squad())
            .verify()
    }
}
