package app.choppa.domain.rotation.smr

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad

typealias SquadPairingPoints = HashMap<SquadPairing, Int>

data class SquadPairing(
    val squad: Squad,
    val member: Member,
)

fun calculateSquadPairingPoints(squadConfigurationsAndDurations: List<Pair<Squad, List<Pair<Int, List<Member>>>>>): SquadPairingPoints {
    val squadParingPoints = SquadPairingPoints()
    val squads = squadConfigurationsAndDurations.map { it.first }
    val members = squadConfigurationsAndDurations
        .asSequence().map { it.second }
        .flatten().map { it.second }
        .flatten().distinct().toList()

    squads.forEach { squad ->
        members.forEach { member ->
            squadParingPoints[SquadPairing(squad, member)] = 0
        }
    }

    squadConfigurationsAndDurations.forEach { configurations ->
        val squad = configurations.first
        val configurationInstance = configurations.second

        configurationInstance.forEach { configuration ->
            val duration = configuration.first
            configuration.second.forEach { member ->
                squadParingPoints[SquadPairing(squad, member)] = + duration
            }
        }
    }

    return squadParingPoints
}
