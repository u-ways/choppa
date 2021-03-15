package app.choppa.acceptance.domain.member

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.domain.member.Member.Companion.NO_MEMBERS
import app.choppa.domain.squad.Squad
import app.choppa.support.factory.ChapterFactory
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.jupiter.api.Test

class MemberTest {
    @Test
    internal fun `NO_MEMBERS static should not pass by reference`() {
        val member = MemberFactory.create()
        val newNoMembersMutableList = NO_MEMBERS

        newNoMembersMutableList.add(member)

        assert(NO_MEMBERS.isEmpty())
    }

    @Test
    fun `verify the equals and hashCode contract`() {
        EqualsVerifier
            .configure().suppress(Warning.SURROGATE_KEY)
            .forClass(Member::class.java)
            .withPrefabValues(Chapter::class.java, ChapterFactory.create(), ChapterFactory.create())
            .withPrefabValues(
                Squad::class.java,
                SquadFactory.create(),
                SquadFactory.create(),
            )
            .verify()
    }
}
