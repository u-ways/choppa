package app.choppa.domain.squad

import app.choppa.domain.account.Account
import app.choppa.domain.account.Account.Companion.UNASSIGNED_ACCOUNT
import app.choppa.domain.member.Member
import app.choppa.domain.member.Member.Companion.NO_MEMBERS
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.Tribe.Companion.UNASSIGNED_TRIBE
import app.choppa.utils.Color.Companion.GREY
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.UUID.randomUUID

@Service
class SquadFactory(
    @Autowired private val squadService: SquadService,
) {
    fun create(
        id: UUID = randomUUID(),
        name: String = "SQ-$id".substring(0, 15),
        color: Int = GREY,
        tribe: Tribe = UNASSIGNED_TRIBE,
        members: MutableList<Member> = NO_MEMBERS,
        account: Account = UNASSIGNED_ACCOUNT,
    ): Squad = squadService.save(Squad(id, name, color, tribe, members), account)

    fun create(
        squads: List<Triple<String, Int, MutableList<Member>>>,
        sharedTribe: Tribe = UNASSIGNED_TRIBE,
        sharedAccount: Account = UNASSIGNED_ACCOUNT,
    ): MutableList<Squad> = squads.map { (name, color, members) ->
        create(name = name, color = color, tribe = sharedTribe, members = members, account = sharedAccount)
    }.toMutableList()
}
