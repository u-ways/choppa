package org.choppa.acceptance.model.member

import org.choppa.model.member.Member
import org.choppa.model.member.Member.Companion.NO_MEMBERS
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
