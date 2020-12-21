package app.choppa.core.rotation.filter

import app.choppa.domain.member.Member
import kotlin.random.Random

internal fun random(members: List<MutableList<Member>>, amount: Int): List<MutableList<Member>> {
    val selectedMembers = MutableList<MutableList<Member>>(members.size) { mutableListOf() }

    val indexedMembers = members.mapIndexed { index, squadMembers ->
        squadMembers.map { Pair(index, it) }.toMutableList()
    }.flatten().toMutableList()

    (0 until amount).map {
        val selectedMember = Random.nextInt(indexedMembers.count())
        val member = indexedMembers[selectedMember]
        indexedMembers.remove(member)
        member
    }.forEach {
        selectedMembers[it.first].add(it.second)
    }

    return selectedMembers.toList()
}
