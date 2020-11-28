package org.choppa.acceptance.domain.tribe

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.choppa.domain.history.History
import org.choppa.domain.iteration.Iteration
import org.choppa.domain.member.Member
import org.choppa.domain.squad.Squad
import org.choppa.domain.tribe.Tribe
import org.junit.jupiter.api.Test

class TribeTest {
    @Test
    fun `verify the equals and hashCode contract`() {
        EqualsVerifier
            .configure().suppress(Warning.SURROGATE_KEY)
            .forClass(Tribe::class.java)
            .withPrefabValues(Squad::class.java, Squad(), Squad())
            .withPrefabValues(History::class.java, History(Iteration(), Tribe(), Squad(), Member()), History(Iteration(), Tribe(), Squad(), Member()))
            .verify()
    }
}
