package app.choppa.core.rotation.filter

import app.choppa.domain.member.Member

enum class Filter : Contract {
    NONE {
        override fun invoke(members: MutableList<Member>, amount: Int): MutableList<Member> = none(members, amount)
    },
    OLDEST {
        override fun invoke(members: MutableList<Member>, amount: Int): MutableList<Member> = oldest(members, amount)
    },
    RANDOM {
        override fun invoke(members: MutableList<Member>, amount: Int): MutableList<Member> = random(members, amount)
    };
}

private fun interface Contract {
    fun invoke(members: MutableList<Member>, amount: Int): MutableList<Member>
}
