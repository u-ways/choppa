package app.choppa.acceptance.domain.iteration

import app.choppa.domain.history.History
import app.choppa.domain.iteration.Iteration
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.jupiter.api.Test

class IterationTest {
    @Test
    fun `verify the equals and hashCode contract`() {
        EqualsVerifier
            .configure().suppress(Warning.SURROGATE_KEY)
            .forClass(Iteration::class.java)
            .withPrefabValues(History::class.java, History(Iteration(), Tribe(), Squad(), Member()), History(Iteration(), Tribe(), Squad(), Member()))
            .verify()
    }
}
