package app.choppa.domain.rotation.smr

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad

class SmartMemberRotation(
    private val squads: List<Squad>,
    private val amount: Int,
    private val chapter: Chapter,
    private val squadsRevisionsAndMemberDuration: List<Pair<Squad, List<Pair<Int, List<Member>>>>>,
    private val mppScalar: Float = 0.1f,
    private val sppScalar: Float = 1.0f,
) {
    private val mpp = calculateMemberPairingPoints(squadsRevisionsAndMemberDuration.map { it.second }.flatten())
    private val spp = calculateSquadPairingPoints(squadsRevisionsAndMemberDuration)
    private val memberToCurrentSquadMap = squadsRevisionsAndMemberDuration.mapToMemberCurrentSquad()
    private val tempSquadCandidatesToRotate = squads.findCandidatesToRotate(chapter)
    private val candidatesToRotate = tempSquadCandidatesToRotate.map { it.members }.flatten().toMutableList()
    private val proposedMoves = mutableListOf<MoveToSquad>()

    fun invoke(): List<MoveToSquad> {
        if (amount < 1) return listOf()

        (0 until amount).forEach { _ ->
            val primaryOptimalMove = candidatesToRotate.map { member ->
                val optimalMove = calculateOptimalMove(member, tempSquadCandidatesToRotate)
                optimalMove.toOptimalMoveSummary(member, tempSquadCandidatesToRotate, optimalMove.first)
            }.minusCandidatesMovingToTheirOriginalSquad(memberToCurrentSquadMap)
                .run {
                    if (isEmpty()) return proposedMoves
                    else handleMove(maxByOrNull { move -> move.ksp })
                }

            val secondaryOptimalMove = primaryOptimalMove.minusAlreadyRotatedCandidates(candidatesToRotate).map { member ->
                val optimalMove = calculateOptimalMove(member, listOf(primaryOptimalMove.squadToMoveTo))
                optimalMove.toOptimalMoveSummary(member, tempSquadCandidatesToRotate, getSquad(primaryOptimalMove.member))
            }.run {
                if (isEmpty()) return proposedMoves
                else handleMove(maxByOrNull { move -> move.ksp })
            }

            swapMembers(primaryOptimalMove.member, secondaryOptimalMove.member)
            tempSquadCandidatesToRotate.removeIf { it.members.none { x -> candidatesToRotate.contains(x) } }
        }

        return proposedMoves
    }

    fun List<Pair<Squad, List<Pair<Int, List<Member>>>>>.mapToMemberCurrentSquad(): HashMap<Member, Squad> =
        HashMap<Member, Squad>().also {
            map { (squad, durations) ->
                durations[0].second.forEach { member -> it[member] = squad }
            }
        }

    fun List<Squad>.findCandidatesToRotate(chapter: Chapter): MutableList<SquadDefinition> = map {
        Pair(it, it.members.toList().filter { x -> x.chapter == chapter }) // && x.squads.size > 1
    }.filter { it.second.count() > 0 }.map { SquadDefinition(it.first, it.second.toMutableList()) }
        .toMutableList()

    private fun averageMpp(x: Member, candidatesToRotate: MutableList<Member>): Double =
        candidatesToRotate.filter { y -> y != x }.map { y -> mpp[MemberPairing(x, y)]!! }.average()

    private fun averageMppByTeam(
        x: Member,
        teams: List<SquadDefinition>,
        averageMpp: Double
    ): List<Pair<Squad, Double>> =
        teams.map {
            Pair(
                it.squad,
                it.members
                    .filter { m -> m != x }
                    .map { y -> mpp[MemberPairing(x, y)]!! - averageMpp }.average()
            )
        }

    private fun memberSppAndMppByTeam(
        x: Member,
        averageMppByTeam: List<Pair<Squad, Double>>
    ): List<Triple<Squad, Int, Double>> =
        averageMppByTeam.map { y ->
            Triple(
                y.first,
                spp[SquadPairing(getSquad(x), x)]!! - spp[SquadPairing(y.first, x)]!!,
                y.second
            )
        }

    fun calculateOptimalMove(member: Member, tempSquadCandidatesToRotate: List<SquadDefinition>): Pair<Squad, Double> {
        val averageMpp = averageMpp(member, candidatesToRotate)
        val averageMppByTeam = averageMppByTeam(member, tempSquadCandidatesToRotate, averageMpp)
        val memberSppAndMppByTeam = memberSppAndMppByTeam(member, averageMppByTeam)
        return memberSppAndMppByTeam.map { x ->
            Pair(x.first, calculateKSP(x.second.toDouble(), x.third))
        }.maxByOrNull { it.second } ?: error("Could not find optimal move for member [${member.id}].")
    }

    fun calculateKSP(spp: Double, mpp: Double): Double = (spp * sppScalar) + (-mpp * mppScalar)

    fun handleMove(move: OptimalMoveSummary?): OptimalMoveSummary {
        check(move != null) { "Expected to find OptimalMoveSummary to handle a move" }
        val member = move.member
        val squad = move.squadToMoveTo.squad
        proposedMoves.add(MoveToSquad(member, getSquad(member), squad))
        candidatesToRotate.remove(member)
        findSquadDefinition(getSquad(member)).members.remove(member)
        return move
    }

    fun swapMembers(member1: Member, member2: Member) {
        findSquadDefinition(getSquad(member1)).members.add(member2)
        findSquadDefinition(getSquad(member2)).members.add(member1)
    }

    fun findSquadDefinition (squad: Squad): SquadDefinition =
        tempSquadCandidatesToRotate.find { it.squad == squad }
            ?: error("Expected to find squad [${squad.id}] within squad candidate list.")

    fun getSquad(member: Member): Squad =
        memberToCurrentSquadMap[member] ?: error("Expected to find squad that member [${member.id}] belongs to.")
}


