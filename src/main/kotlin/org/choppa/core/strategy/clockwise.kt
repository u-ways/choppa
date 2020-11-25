package org.choppa.core.strategy

import org.choppa.domain.member.Member
import java.util.Collections

internal fun clockwise(members: List<List<Member>>): List<List<Member>> = members.apply {
    // TODO(u-ways) #52 maybe implement our own right shift if this have bad performance?
    Collections.rotate(this, 1)
}
