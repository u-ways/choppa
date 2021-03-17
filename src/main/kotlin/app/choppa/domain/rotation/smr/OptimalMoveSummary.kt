package app.choppa.domain.rotation.smr

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad

data class OptimalMoveSummary(
    val member: Member,
    val squadToMoveTo: SquadDefinition,
    val ksp: Double
)

internal fun List<OptimalMoveSummary>.minusCandidatesMovingToTheirOriginalSquad(
    memberToCurrentSquadMap: HashMap<Member, Squad>
) = this.filter { x -> x.squadToMoveTo.squad != memberToCurrentSquadMap[x.member]!! }

internal fun OptimalMoveSummary.minusAlreadyRotatedCandidates(candidatesToRotate: MutableList<Member>) =
    squadToMoveTo.members.filter { candidatesToRotate.contains(it) }

internal fun Pair<Squad, Double>.toOptimalMoveSummary(
    member: Member,
    tempSquadCandidatesToRotate: MutableList<SquadDefinition>,
    squad: Squad
) = OptimalMoveSummary(
    member, tempSquadCandidatesToRotate
        .find { it.squad == squad }
        ?.let { (squad, members) -> SquadDefinition(squad, members) }
        ?: error("Expected to find squad [${squad.id}] within tempSquadCandidatesToRotate"), second
)
