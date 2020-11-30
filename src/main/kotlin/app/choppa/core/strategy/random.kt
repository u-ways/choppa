package app.choppa.core.strategy

import app.choppa.domain.member.Member
import java.lang.Math.floorDiv

internal fun random(members: List<List<Member>>): List<List<Member>> {
    val rotated = floorDiv(members.flatten().count(), members.count())

    val randomisedMembers =
        members.asSequence().mapIndexed { position, memberList ->
            memberList.map { y -> Triple(Math.random(), position, y) }
        }.flatten().sortedBy { it.first }.toMutableList()

    randomisedMembers.forEachIndexed { position, member ->
        val theoreticallyAssignedSquad = floorDiv(position, rotated)
        if (theoreticallyAssignedSquad == member.second) {
            val swapper = (0 until randomisedMembers.count()).first { j ->
                val tempAssignedSquad = floorDiv(j, rotated)
                tempAssignedSquad != theoreticallyAssignedSquad && randomisedMembers[j].second != theoreticallyAssignedSquad
            }
            val swapperVal = randomisedMembers[swapper]
            val tempDouble = swapperVal.first
            randomisedMembers[position] = Triple(member.first, swapperVal.second, swapperVal.third)
            randomisedMembers[swapper] = Triple(tempDouble, member.second, member.third)
        }
    }

    return randomisedMembers.map { it.third }.windowed(rotated, rotated)
}
