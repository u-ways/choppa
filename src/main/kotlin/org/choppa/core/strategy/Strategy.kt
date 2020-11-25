package org.choppa.core.strategy

import org.choppa.domain.member.Member

enum class Strategy : Contract {
    CLOCKWISE {
        override fun invoke(members: List<List<Member>>): List<List<Member>> = clockwise(members)
    },
    ANTI_CLOCKWISE {
        override fun invoke(members: List<List<Member>>): List<List<Member>> = antiClockwise(members)
    },
    RANDOM {
        override fun invoke(members: List<List<Member>>): List<List<Member>> = random(members)
    };
}

private fun interface Contract {
    fun invoke(members: List<List<Member>>): List<List<Member>>
}
