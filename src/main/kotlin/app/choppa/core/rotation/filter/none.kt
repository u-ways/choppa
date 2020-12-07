package app.choppa.core.rotation.filter

import app.choppa.domain.member.Member

internal fun none(members: MutableList<Member>, amount: Int): MutableList<Member> =
    members.take(amount).toMutableList()
