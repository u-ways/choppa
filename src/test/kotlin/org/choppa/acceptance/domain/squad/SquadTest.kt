package org.choppa.acceptance.domain.squad

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.choppa.domain.chapter.Chapter
import org.choppa.domain.history.History
import org.choppa.domain.iteration.Iteration
import org.choppa.domain.member.Member
import org.choppa.domain.squad.Squad
import org.choppa.domain.tribe.Tribe
import org.junit.jupiter.api.Test

class SquadTest {
    @Test
    fun `verify the equals and hashCode contract`() {
        EqualsVerifier
            .configure().suppress(Warning.SURROGATE_KEY)
            .forClass(Squad::class.java)
            .withPrefabValues(Member::class.java, Member(), Member())
            .withPrefabValues(Chapter::class.java, Chapter(), Chapter())
            .withPrefabValues(Tribe::class.java, Tribe(), Tribe())
            .withPrefabValues(History::class.java, History(Iteration(), Tribe(), Squad(), Member()), History(Iteration(), Tribe(), Squad(), Member()))
            .verify()
    }
}
