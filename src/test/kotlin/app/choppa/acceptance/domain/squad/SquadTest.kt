package app.choppa.acceptance.domain.squad

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
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
            .verify()
    }
}
