package app.choppa.domain.rotation.filter

import app.choppa.domain.member.Member
import kotlin.random.Random

internal fun random(candidates: List<List<Member>>, amount: Int): List<MutableList<Member>> {
    val selectedMembers = MutableList<MutableList<Member>>(candidates.size) { mutableListOf() }

    val indexedMembers = candidates.mapIndexed { index, squadMembers ->
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

    return selectedMembers
}
