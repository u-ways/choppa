package app.choppa.domain.rotation.strategy

import app.choppa.domain.member.Member

internal fun antiClockwise(members: List<List<Member>>): List<List<Member>> =
    members.drop(1) + members.take(1)
