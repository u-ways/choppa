package app.choppa.core.rotation.strategy

import app.choppa.domain.member.Member
import kotlin.math.floor

internal fun random(members: List<List<Member>>): List<List<Member>> {
    val selectedMembers = MutableList<MutableList<Member>>(members.size) { mutableListOf() }

    val indexedMembers = members.mapIndexed { index, squadMembers ->
        squadMembers.map { Pair(index, it) }.toMutableList()
    }.flatten().toMutableList()

    // Made one smaller to give leeway if member is reassigned to their original squad.
    val squadCount = members.count() - 1

    indexedMembers.map { member ->
        var newSquad = floor(Math.random() * squadCount).toInt()
        if (newSquad == member.first) newSquad++
        Pair(newSquad, member.second)
    }.forEach {
        selectedMembers[it.first].add(it.second)
    }

    return selectedMembers.map { it.toList() }.toList()
}
