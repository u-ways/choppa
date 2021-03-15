package app.choppa.acceptance.domain.chapter

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.support.base.Universe
import app.choppa.support.factory.MemberFactory
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.jupiter.api.Test

class ChapterTest : Universe() {
    @Test
    fun `verify the equals and hashCode contract`() {
        EqualsVerifier
            .configure().suppress(Warning.SURROGATE_KEY)
            .forClass(Chapter::class.java)
            .withPrefabValues(Member::class.java, MemberFactory.create(), MemberFactory.create())
            .verify()
    }
}
