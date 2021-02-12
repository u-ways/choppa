package app.choppa.domain.rotation.strategy

import app.choppa.domain.member.Member

internal fun clockwise(members: List<List<Member>>): List<List<Member>> =
    members.takeLast(1) + members.dropLast(1)
