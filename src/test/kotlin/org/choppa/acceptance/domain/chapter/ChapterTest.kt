package org.choppa.acceptance.domain.chapter

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.choppa.domain.chapter.Chapter
import org.choppa.domain.member.Member
import org.junit.jupiter.api.Test

class ChapterTest {
    @Test
    fun `verify the equals and hashCode contract`() {
        EqualsVerifier
            .configure().suppress(Warning.SURROGATE_KEY)
            .forClass(Chapter::class.java)
            .withPrefabValues(Member::class.java, Member(), Member())
            .verify()
    }
}
