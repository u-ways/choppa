package app.choppa.support.factory

import app.choppa.domain.account.Account
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.Squad.Companion.NO_SQUADS
import app.choppa.domain.tribe.Tribe
import app.choppa.support.base.Universe
import app.choppa.utils.Color.Companion.GREY
import java.util.*
import java.util.UUID.randomUUID

class TribeFactory {
    companion object : Universe() {
        /**
         * Creates a random Tribe.
         */
        fun create(
            id: UUID = randomUUID(),
            name: String = "TR-$id".substring(0, 15),
            color: Int = GREY,
            squads: MutableList<Squad> = NO_SQUADS,
            account: Account = ACCOUNT,
        ): Tribe = Tribe(id, name, color, squads, account)

        /**
         * Create X amount of tribes with mutual attributes.
         */
        fun create(
            amount: Int,
            sharedAccount: Account = ACCOUNT,
        ): MutableList<Tribe> = (0 until amount).map {
            create(account = sharedAccount)
        }.toMutableList()
    }
}
