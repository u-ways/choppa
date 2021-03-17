package app.choppa.domain.rotation.smr

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad

data class SquadDefinition(
    val squad: Squad,
    val members: MutableList<Member>
)
