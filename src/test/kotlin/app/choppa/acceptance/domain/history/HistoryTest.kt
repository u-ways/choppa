package app.choppa.acceptance.domain.history

import app.choppa.domain.history.History
import app.choppa.domain.iteration.Iteration
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.jupiter.api.Test

class HistoryTest {
    @Test
    fun `verify the equals and hashCode contract`() {
        EqualsVerifier
            .configure().suppress(Warning.SURROGATE_KEY)
            .forClass(History::class.java)
            .withPrefabValues(Iteration::class.java, Iteration(), Iteration())
            .withPrefabValues(Tribe::class.java, Tribe(), Tribe())
            .withPrefabValues(Squad::class.java, Squad(), Squad())
            .withPrefabValues(Member::class.java, Member(), Member())
            .verify()
    }
}
