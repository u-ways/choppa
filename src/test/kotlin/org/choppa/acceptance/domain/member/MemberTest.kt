package org.choppa.acceptance.domain.member

import org.choppa.domain.member.Member
import org.choppa.domain.member.Member.Companion.NO_MEMBERS
import org.junit.jupiter.api.Test

class MemberTest {
    @Test
    internal fun `NO_MEMBERS static should not pass by reference`() {
        val member = Member()
        val newNoMembersMutableList = NO_MEMBERS

        newNoMembersMutableList.add(member)

        assert(NO_MEMBERS.isEmpty())
    }
}
