package app.choppa.domain.rotation.smr

import app.choppa.domain.member.Member

typealias MemberPairingPoints = HashMap<MemberPairing, Int>

data class MemberPairing(
    val member_1: Member,
    val member_2: Member,
)

fun calculateMemberPairingPoints(squadsAndDurations: List<Pair<Int, List<Member>>>): MemberPairingPoints {
    val memberParingPoints = MemberPairingPoints()

    squadsAndDurations.map {
        it.second
    }.flatten().distinct().apply {
        forEachIndexed { index, member ->
            if (index + 1 < size) {
                subList(index + 1, size).forEach { subMember ->
                    memberParingPoints[
                        MemberPairing(
                            member_1 = member,
                            member_2 = subMember
                        )
                    ] = 0

                    memberParingPoints[
                        MemberPairing(
                            member_1 = subMember,
                            member_2 = member
                        )
                    ] = 0
                }
            }
        }
    }

    squadsAndDurations.forEach {
        val time = it.first
        val members = it.second

        members.forEachIndexed { index, member ->
            if (index + 1 < members.size) {
                members.subList(index + 1, members.size).forEach { subMember ->
                    memberParingPoints[
                        MemberPairing(
                            member_1 = member,
                            member_2 = subMember
                        )
                    ] = + time

                    memberParingPoints[
                        MemberPairing(
                            member_1 = subMember,
                            member_2 = member
                        )
                    ] = + time
                }
            }
        }
    }
    return memberParingPoints
}
