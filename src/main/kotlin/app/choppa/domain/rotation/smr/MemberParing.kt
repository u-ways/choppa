package app.choppa.domain.rotation.smr

import app.choppa.domain.member.Member

typealias MemberParingPoints = HashMap<MemberParing, Int>

data class MemberParing(
    val member_1: Member,
    val member_2: Member,
)

fun calculateMemberPairingPoints(squadsAndDurations: List<Pair<Int, List<Member>>>): MemberParingPoints {
    val memberParingPoints = MemberParingPoints()

    squadsAndDurations.map {
        it.second
    }.flatten().distinct().apply {
        forEachIndexed { index, member ->
            if (index + 1 < size) {
                subList(index + 1, size).forEach { subMember ->
                    memberParingPoints[MemberParing(
                        member_1 = member,
                        member_2 = subMember
                    )] = 0

                    memberParingPoints[MemberParing(
                        member_1 = subMember,
                        member_2 = member
                    )] = 0
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
                    memberParingPoints[MemberParing(
                        member_1 = member,
                        member_2 = subMember
                    )]?.plus(time)!!

                    memberParingPoints[MemberParing(
                        member_1 = subMember,
                        member_2 = member
                    )]?.plus(time)!!
                }
            }
        }
    }
    return memberParingPoints
}
