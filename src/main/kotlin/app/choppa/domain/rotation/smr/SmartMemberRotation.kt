package app.choppa.domain.rotation.smr

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad

class SmartMemberRotation {
    companion object {
        var mppScalar = 0.1f
        var sppScalar = 1.0f

        fun invoke(
            squads: List<Squad>,
            amount: Int,
            chapter: Chapter,
            squadsRevisionsAndMemberDuration: List<Pair<Squad, List<Pair<Int, List<Member>>>>>,
        ): List<MoveToSquad> {
            if (amount < 1) return listOf()

            val memberToCurrentSquadMap = HashMap<Member, Squad>().apply {
                squadsRevisionsAndMemberDuration.map { (squad, durations) ->
                    durations[0].second.forEach { this[it] = squad }
                }
            }

            val currentMemberSquads = squads.map {
                Pair(it, it.members.toList().filter { x -> x.chapter == chapter }) // && x.squads.size > 1
            }.filter { it.second.count() > 0 }

            val tempMemberSquads = currentMemberSquads.map { Pair(it.first, it.second.toMutableList()) }.toMutableList()
            val activeMemberList = currentMemberSquads.map { it.second }.flatten().toMutableList()
            val mpp = calculateMemberPairingPoints(squadsRevisionsAndMemberDuration.map { it.second }.flatten())
            val spp = calculateSquadPairingPoints(squadsRevisionsAndMemberDuration)

            val proposedMoves = mutableListOf<MoveToSquad>()

            fun averageMpp(x: Member): Double =
                activeMemberList.filter { y -> y != x }.map { y -> mpp[MemberPairing(x, y)]!! }.average()

            fun averageMppByTeam(
                x: Member,
                teams: List<Pair<Squad, List<Member>>>,
                averageMpp: Double
            ): List<Pair<Squad, Double>> =
                teams.map {
                    Pair(
                        it.first,
                        it.second.filter { m -> m != x }.map { y -> mpp[MemberPairing(x, y)]!! - averageMpp }.average()
                    )
                }

            fun memberSppAndMppByTeam(
                x: Member,
                averageMppByTeam: List<Pair<Squad, Double>>
            ): List<Triple<Squad, Int, Double>> =
                averageMppByTeam.map { y ->
                    Triple(
                        y.first,
                        spp[SquadPairing(memberToCurrentSquadMap[x]!!, x)]!! - spp[SquadPairing(y.first, x)]!!,
                        y.second
                    )
                }

            fun optimalMove(conditions: List<Triple<Squad, Int, Double>>): Pair<Squad, Double> =
                conditions.map { x -> Pair(x.first, (x.second * sppScalar) + (-x.third * mppScalar)) }
                    .maxByOrNull { it.second }!!

            fun handleMove(move: Triple<Member, Pair<Squad, List<Member>>, Double>) {
                proposedMoves.add(MoveToSquad(move.first, memberToCurrentSquadMap[move.first]!!, move.second.first))
                activeMemberList.remove(move.first)
                tempMemberSquads.find { it.first == memberToCurrentSquadMap[move.first]!! }!!.second.remove(move.first)
            }

            (0 until amount).forEach { _ ->
                val ksps: List<Triple<Member, Pair<Squad, List<Member>>, Double>> = activeMemberList
                    .map { x ->
                        val memberSppAndMppByTeam =
                            memberSppAndMppByTeam(x, averageMppByTeam(x, tempMemberSquads, averageMpp(x)))
                        val optimalMove: Pair<Squad, Double> = optimalMove(memberSppAndMppByTeam)
                        Triple(x, tempMemberSquads.find { it.first == optimalMove.first }!!, optimalMove.second)
                    }.filter { x -> x.second.first != memberToCurrentSquadMap[x.first]!! }

                if (ksps.isEmpty()) return proposedMoves
                val best = ksps.maxByOrNull { x -> x.third }!!
                handleMove(best)

                val secondaryKSPs = best.second.second.filter { activeMemberList.contains(it) }.map { x ->
                    val memberSppAndMppByTeam =
                        memberSppAndMppByTeam(x, averageMppByTeam(x, listOf(best.second), averageMpp(x)))
                    val optimalMove: Pair<Squad, Double> = optimalMove(memberSppAndMppByTeam)
                    Triple(x, tempMemberSquads.find { it.first == memberToCurrentSquadMap[best.first]!! }!!, optimalMove.second)
                }

                if (secondaryKSPs.isEmpty()) return proposedMoves
                val bestSecondary = secondaryKSPs.maxByOrNull { x -> x.third }!!
                handleMove(bestSecondary)

                tempMemberSquads.find { it.first == memberToCurrentSquadMap[best.first]!! }!!.second.add(bestSecondary.first)
                tempMemberSquads.find { it.first == memberToCurrentSquadMap[bestSecondary.first]!! }!!.second.add(best.first)

                tempMemberSquads.removeIf { it.second.none { x -> activeMemberList.contains(x) } }
            }

            return proposedMoves
        }
    }
}
