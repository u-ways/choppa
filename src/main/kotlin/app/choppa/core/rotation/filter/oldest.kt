package app.choppa.core.rotation.filter

import app.choppa.domain.member.Member

internal fun oldest(members: List<MutableList<Member>>, amount: Int): List<MutableList<Member>> {
    val selectedMembers = mutableListOf<MutableList<Member>>()
    members.forEach { _ -> selectedMembers.add(mutableListOf()) }

    members.mapIndexed { index, squadMembers ->
        squadMembers.map { Pair(index, it) }
    }.flatten().apply {
        if (this.count() > 1) this.sortedBy { it.second.history.last().iteration.startDate }
    }.take(amount).forEach {
        selectedMembers[it.first].add(it.second)
    }

    return selectedMembers.toList()
}
