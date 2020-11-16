package org.choppa.core

import org.choppa.domain.chapter.Chapter
import org.choppa.domain.member.Member
import org.choppa.domain.squad.Squad
import org.choppa.domain.tribe.Tribe
import java.util.Collections

class BaseRotation {
    companion object {
        fun rotate(tribe: Tribe, chapter: Chapter, amountOfMembersToRotate: Int = 1, clockWise: Boolean): Tribe {
            if (tribe.squads.count() < 2) return tribe

            val squadCandidatesToRotate =
                tribe.squads.map { squad ->
                    val candidates = squad.members
                        .filter { member -> member.chapter == chapter && member.history.isNotEmpty() }.toMutableList()
                    if (candidates.count() > 1) candidates.sortBy { member -> member.history.last().iteration.startDate }
                    Pair(squad, candidates)
                }

            val squadCount = tribe.squads.count()
            val newPositions = mutableListOf<List<Member>>()

            squadCandidatesToRotate.forEach {
                val members = it.second
                val plannedRotations = Math.min(amountOfMembersToRotate, members.count())
                val newMembers = (0 until plannedRotations).map { memberIndex -> members.removeAt(memberIndex) }
                newPositions.add(newMembers)
            }

            Collections.rotate(
                newPositions, if (clockWise) {
                    1
                } else {
                    -1
                }
            )
            (0 until squadCount).map { squadIndex ->
                val (_, members) = squadCandidatesToRotate[squadIndex]
                members.addAll(newPositions[squadIndex])
            }

            val newSquads = squadCandidatesToRotate.map { (squad, members) ->
                Squad(squad.id, squad.name, squad.color, squad.tribe, members, squad.history)
            }.toMutableList()

            return Tribe(tribe.id, tribe.name, tribe.color, newSquads, tribe.history)
        }

        fun randomRotate(tribe: Tribe, chapter: Chapter, amountOfMembersToRotate: Int = 1): Tribe {
            if (tribe.squads.count() < 2) return tribe

            val squadCandidatesToRotate =
                tribe.squads.map { squad ->
                    val candidates = squad.members
                        .filter { member -> member.chapter == chapter && member.history.isNotEmpty() }.toMutableList()
                    if (candidates.count() > 1) candidates.sortBy { member -> member.history.last().iteration.startDate }
                    Pair(squad, candidates)
                }

            val squadCount = tribe.squads.count()
            val removedMembers = mutableListOf<MutableList<Member>>()

            squadCandidatesToRotate.forEach {
                val members = it.second
                val plannedRotations = Math.min(amountOfMembersToRotate, members.count())
                val movedMembers =
                    (0 until plannedRotations).map { memberIndex -> members.removeAt(memberIndex) }.toMutableList()
                removedMembers.add(movedMembers)
            }

            val newMembers = mutableListOf<MutableList<Member>>()
            (0 until squadCount).forEach { _ -> newMembers.add(mutableListOf()) }
            (0 until squadCount).forEach { i ->
                val squad = removedMembers[i]
                val subSquadCount = squad.count()
                (0 until subSquadCount).forEach {
                    var moveTo = 0
                    do {
                        moveTo = (0 until squadCount - 1).random()
                        if (moveTo == i) moveTo++
                    } while (newMembers[moveTo].count() >= amountOfMembersToRotate)
                    newMembers[moveTo].add(squad.removeAt(0))
                }
            }

            (0 until squadCount).map { squadIndex ->
                val (_, members) = squadCandidatesToRotate[squadIndex]
                members.addAll(newMembers[squadIndex])
            }

            val newSquads = squadCandidatesToRotate.map { (squad, members) ->
                Squad(squad.id, squad.name, squad.color, squad.tribe, members, squad.history)
            }.toMutableList()

            return Tribe(tribe.id, tribe.name, tribe.color, newSquads, tribe.history)
        }
    }
}
