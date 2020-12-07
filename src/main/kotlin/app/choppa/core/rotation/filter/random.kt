package app.choppa.core.rotation.filter

import app.choppa.domain.member.Member
import kotlin.random.Random

internal fun random(members: MutableList<Member>, amount: Int): MutableList<Member> {
    val memberCount = members.count()

    //Handles empty list, doesn't do random selection if amount is too small.
    if(memberCount <= amount) {
        return members.take(memberCount).toMutableList()
    }

    //To not alter the original list.
    val tempMembers = members.toMutableList()
    val createNewList = amount < memberCount / 2

    if(!createNewList) {
        val removalAmount = memberCount - amount
        (0 until removalAmount).forEach { _ ->
            tempMembers.removeAt(Random.nextInt(tempMembers.count()))
        }
        return tempMembers
    }

    return (0 until amount).map {
        val selectedMember = Random.nextInt(tempMembers.count())
        val member = tempMembers[selectedMember]
        tempMembers.remove(member)
        member
    }.toMutableList()
}
