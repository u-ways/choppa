package app.choppa.core.rotation.filter

import app.choppa.domain.member.Member

internal fun oldest(members: MutableList<Member>, amount: Int): MutableList<Member> = members.toMutableList().apply {
    if (this.count() > 1) this.sortBy { it.history.last().iteration.startDate }
}.take(amount).toMutableList()
