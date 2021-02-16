package app.choppa.domain.rotation.filter

import app.choppa.domain.member.Member

internal fun distributed(candidates: List<List<Member>>, amount: Int): List<List<Member>> =
    if (candidates.flatten().count() < 2) candidates
    else MutableList<MutableList<Member>>(candidates.size) { mutableListOf() }.apply {
        val squadCount = candidates.count()
        var count = 0
        var i = 0

        while (count < amount) {
            val squadIndex = i % squadCount
            val memberIndex = (i - squadIndex) / squadCount
            val selectedSquad = candidates[squadIndex]
            if (selectedSquad.count() > memberIndex) {
                this[squadIndex].add(selectedSquad[memberIndex])
                ++count
            }
            ++i
        }
    }
