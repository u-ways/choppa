package app.choppa.support.factory

import app.choppa.domain.account.Account
import app.choppa.domain.member.Member
import app.choppa.domain.member.Member.Companion.NO_MEMBERS
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import app.choppa.support.base.Universe
import app.choppa.utils.Color.Companion.GREY
import java.util.*
import java.util.UUID.randomUUID

class SquadFactory {
    companion object : Universe() {
        /**
         * Creates a random Squad.
         */
        fun create(
            id: UUID = randomUUID(),
            name: String = "SQ-$id".substring(0, 15),
            color: Int = GREY,
            tribe: Tribe = TRIBE,
            members: MutableList<Member> = NO_MEMBERS,
            account: Account = ACCOUNT,
        ): Squad = Squad(id, name, color, tribe, members, account)

        /**
         * Create X amount of squads with mutual attributes.
         */
        fun create(
            amount: Int,
            sharedTribe: Tribe = TRIBE,
            sharedAccount: Account = ACCOUNT,
        ): MutableList<Squad> = (0 until amount).map {
            create(tribe = sharedTribe, account = sharedAccount)
        }.toMutableList()
    }
}
