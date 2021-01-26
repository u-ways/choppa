package app.choppa.domain.rotation.strategy

import app.choppa.domain.member.Member

enum class Strategy : Policy {
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

private fun interface Policy {
    fun invoke(members: List<List<Member>>): List<List<Member>>
}
