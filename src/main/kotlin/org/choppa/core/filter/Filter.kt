package org.choppa.core.filter

import org.choppa.domain.member.Member

enum class Filter : Contract {
    NONE {
        override fun invoke(members: MutableList<Member>, amount: Int): MutableList<Member> = members
            .take(amount).toMutableList()
    },
    OLDEST {
        override fun invoke(members: MutableList<Member>, amount: Int): MutableList<Member> = members.apply {
            if (this.count() > 1) this.sortBy { it.history.last().iteration.startDate }
        }.take(amount).toMutableList()
    };
}

private fun interface Contract {
    fun invoke(members: MutableList<Member>, amount: Int): MutableList<Member>
}
