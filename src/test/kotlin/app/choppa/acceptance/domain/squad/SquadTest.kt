package app.choppa.acceptance.domain.squad

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import app.choppa.support.factory.ChapterFactory
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.TribeFactory
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.jupiter.api.Test

class SquadTest {
    @Test
    fun `verify the equals and hashCode contract`() {
        EqualsVerifier
            .configure().suppress(Warning.SURROGATE_KEY)
            .forClass(Squad::class.java)
            .withPrefabValues(Member::class.java, MemberFactory.create(), MemberFactory.create())
            .withPrefabValues(Chapter::class.java, ChapterFactory.create(), ChapterFactory.create())
            .withPrefabValues(Tribe::class.java, TribeFactory.create(), TribeFactory.create())
            .verify()
    }
}
