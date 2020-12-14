package app.choppa.core.rotation.filter

import app.choppa.domain.member.Member

enum class Filter : Policy {
    DISTRIBUTED {
        override fun invoke(members: List<MutableList<Member>>, amount: Int): List<MutableList<Member>> = distributed(members, amount)
    },
    OLDEST {
        override fun invoke(members: List<MutableList<Member>>, amount: Int): List<MutableList<Member>> = oldest(members, amount)
    },
    RANDOM {
        override fun invoke(members: List<MutableList<Member>>, amount: Int): List<MutableList<Member>> = random(members, amount)
    };
}

private fun interface Policy {
    fun invoke(members: List<MutableList<Member>>, amount: Int): List<MutableList<Member>>
}
