package app.choppa.domain.rotation.smr

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad

data class MoveToSquad(
    val member: Member,
    val from: Squad,
    val to: Squad,
)
