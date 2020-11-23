package org.choppa.core

import org.choppa.domain.chapter.Chapter
import org.choppa.domain.member.Member
import org.choppa.domain.squad.Squad
import org.choppa.domain.tribe.Tribe
import java.lang.Math.floorDiv
import java.lang.Math.random
import java.util.Collections

class BaseRotation {
    companion object {
        fun rotate(tribe: Tribe, chapter: Chapter, amountOfMembersToRotate: Int = 1, clockWise: Boolean): Tribe {
            if (tribe.squads.count() < 2) return tribe

            val newList = mutableListOf<List<Member>>()
            val newPositions =
                tribe.squads.map { squad ->
                    val candidates = squad.members.filterMembersBy(chapter).sortByOldestMemberFirst()
                    newList.add(candidates.take(amountOfMembersToRotate))
                    candidates.minus(newList.last())
                }

            Collections.rotate(newList, if (clockWise) 1 else -1)

            val newSquads = newPositions.mapIndexed { index, newSquads ->
                val squad = tribe.squads[index]
                Squad(squad.id, squad.name, squad.color, squad.tribe, newSquads.plus(newList[index]).toMutableList(), squad.history)
            }.toMutableList()

            return Tribe(tribe.id, tribe.name, tribe.color, newSquads, tribe.history)
        }

        fun randomRotate(tribe: Tribe, chapter: Chapter, amountOfMembersToRotate: Int = 1): Tribe {
            if (tribe.squads.count() < 2) return tribe

            val removedMembers =
                tribe.squads.map { it.members.filterMembersBy(chapter).take(amountOfMembersToRotate) }

            val rotated = floorDiv(removedMembers.flatten().count(), removedMembers.count())

            val randomisedMembers =
                removedMembers.asSequence().mapIndexed { position, memberList ->
                    memberList.map { y -> Triple(random(), position, y) }
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

            val finalMembers = randomisedMembers.map { it.third }.windowed(rotated, rotated).toMutableList()

            val newSquads = finalMembers.mapIndexed { squadIndex, members ->
                val squad = tribe.squads[squadIndex]
                val squadMembers = squad.members.toMutableList()
                squadMembers.removeAll(removedMembers[squadIndex])
                squadMembers.addAll(members)
                Squad(squad.id, squad.name, squad.color, squad.tribe, squadMembers, squad.history)
            }.toMutableList()

            return Tribe(tribe.id, tribe.name, tribe.color, newSquads, tribe.history)
        }

        private fun MutableList<Member>.filterMembersBy(chapter: Chapter): MutableList<Member> {
            return this.filter { it.chapter == chapter }.toMutableList()
        }

        private fun MutableList<Member>.sortByOldestMemberFirst(): MutableList<Member> {
            return this.apply {
                if (this.count() > 1) this.sortBy { it.history.last().iteration.startDate }
            }
        }
    }
}
