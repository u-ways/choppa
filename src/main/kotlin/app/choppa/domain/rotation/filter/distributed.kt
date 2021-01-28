package app.choppa.domain.rotation.filter

import app.choppa.domain.member.Member

internal fun distributed(members: List<MutableList<Member>>, amount: Int): List<MutableList<Member>> {
    val squadCount = members.count()
    val selectedMembers = MutableList<MutableList<Member>>(members.size) { mutableListOf() }

    var i = 0
    var count = 0

    while (count < amount) {
        val squadIndex = i % squadCount
        val memberIndex = (i - squadIndex) / squadCount
        val selectedSquad = members[squadIndex]
        if (selectedSquad.count() > memberIndex) {
            selectedMembers[squadIndex].add(selectedSquad[memberIndex])
            ++count
        }
        ++i
    }

    return selectedMembers.toList()
}
