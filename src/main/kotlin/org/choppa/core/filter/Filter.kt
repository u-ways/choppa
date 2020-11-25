package org.choppa.core.filter

import org.choppa.domain.member.Member

enum class Filter : Contract {
    NONE {
        override fun invoke(members: MutableList<Member>): MutableList<Member> = members
    },
    OLDEST {
        override fun invoke(members: MutableList<Member>): MutableList<Member> = members.apply {
            if (this.count() > 1) this.sortBy { it.history.last().iteration.startDate }
        }
    };
}

private fun interface Contract {
    fun invoke(members: MutableList<Member>): MutableList<Member>
}
