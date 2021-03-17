package app.choppa.domain.rotation.filter

import app.choppa.domain.member.Member

enum class Filter : Policy {
    NONE {
        override fun invoke(
            candidates: List<List<Member>>,
            amount: Int
        ): List<List<Member>> = candidates
    },
    DISTRIBUTED {
        override fun invoke(
            candidates: List<List<Member>>,
            amount: Int
        ): List<List<Member>> = distributed(candidates, amount)
    },
    OLDEST {
        override fun invoke(
            candidates: List<List<Member>>,
            amount: Int
        ): List<List<Member>> = oldest(candidates, amount)
    },
    RANDOM {
        override fun invoke(
            candidates: List<List<Member>>,
            amount: Int
        ): List<List<Member>> = random(candidates, amount)
    };
}

private fun interface Policy {
    fun invoke(
        candidates: List<List<Member>>,
        amount: Int
    ): List<List<Member>>
}
