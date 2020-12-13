package app.choppa.core.rotation.filter

import app.choppa.domain.member.Member

internal fun distributed(members: List<MutableList<Member>>, amount: Int): List<MutableList<Member>> {
    val squadCount = members.count()
    val selectedMembers = mutableListOf<MutableList<Member>>()
    (0 until squadCount).forEach{ _ -> selectedMembers.add(mutableListOf()) }

    var i = 0
    var count = 0

    while(count < amount) {
        val squadIndex = i % squadCount
        val memberIndex = (i - squadIndex) / squadCount
        val selectedSquad = members[squadIndex]
        if(selectedSquad.count() > memberIndex) {
            selectedMembers[squadIndex].add(selectedSquad[memberIndex])
            ++count
        }
        ++i
    }

    return selectedMembers.toList()
}
