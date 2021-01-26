package app.choppa.domain.rotation.filter

import app.choppa.domain.member.Member

internal fun oldest(members: List<MutableList<Member>>, amount: Int): List<MutableList<Member>> {
    val selectedMembers = MutableList<MutableList<Member>>(members.size) { mutableListOf() }

    members.mapIndexed { index, squadMembers ->
        squadMembers.map { Pair(index, it) }
    }.flatten().apply {
//        FIXME(u-ways) #175 use Squad revisions to find oldest members
//        if (this.count() > 1) this.sortedBy { it.second.history.last().iteration.startDate }
    }.take(amount).forEach {
        selectedMembers[it.first].add(it.second)
    }

    return selectedMembers.toList()
}
