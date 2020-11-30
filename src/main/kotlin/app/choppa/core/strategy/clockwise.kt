package app.choppa.core.strategy

import app.choppa.domain.member.Member
import java.util.Collections

internal fun clockwise(members: List<List<Member>>): List<List<Member>> = members.apply {
    // TODO(u-ways) #52 maybe implement our own right shift if this have bad performance?
    Collections.rotate(this, 1)
}