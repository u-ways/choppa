package app.choppa.domain.rotation.smr

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad

typealias SquadParingPoints = HashMap<SquadParing, Int>

data class SquadParing(
    val squad: Squad,
    val member: Member,
)

fun calculateSquadPairingPoints(squadConfigurationsAndDurations: List<Pair<Squad, List<Pair<Int, List<Member>>>>>): SquadParingPoints {
    val squadParingPoints = SquadParingPoints()
    val squads = squadConfigurationsAndDurations.map { it.first }
    val members = squadConfigurationsAndDurations
        .asSequence().map { it.second }
        .flatten().map { it.second }
        .flatten().distinct().toList()

    squads.forEach { squad ->
        members.forEach { member ->
            squadParingPoints[SquadParing(squad, member)] = 0
        }
    }

    squadConfigurationsAndDurations.forEach { configurations ->
        val squad = configurations.first
        val configurationInstance = configurations.second

        configurationInstance.forEach { configuration ->
            val duration = configuration.first
            configuration.second.forEach { member ->
                squadParingPoints[SquadParing(squad, member)]?.plus(duration)
            }
        }
    }

    return squadParingPoints
}
