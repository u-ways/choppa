package app.choppa.domain.rotation.filter

import app.choppa.domain.member.Member

internal fun distributed(candidates: List<List<Member>>, amount: Int): List<List<Member>> {
    val squadCount = candidates.count()
    val selectedMembers = MutableList<MutableList<Member>>(candidates.size) { mutableListOf() }

    var i = 0
    var count = 0

    while (count < amount) {
        val squadIndex = i % squadCount
        val memberIndex = (i - squadIndex) / squadCount
        val selectedSquad = candidates[squadIndex]
        if (selectedSquad.count() > memberIndex) {
            selectedMembers[squadIndex].add(selectedSquad[memberIndex])
            ++count
        }
        ++i
    }

    return selectedMembers
}
