package app.choppa.support.factory

import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.Tribe.Companion.UNASSIGNED_TRIBE

class SquadFactory {
    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        /**
         * Creates a random Squad with X amount of members.
         *
         * @param tribe Tribe the squad's tribe. (default = UNASSIGNED_TRIBE)
         * @param membersAmount Int the number of members to the squad has.
         * @return Squad
         */
        fun create(
            membersAmount: Int = 0,
            tribe: Tribe = UNASSIGNED_TRIBE
        ): Squad = Squad(members = MemberFactory.create(membersAmount), tribe = tribe)

        /**
         * Create X amount of squads with Y amount of members in each.
         *
         * @param amount Int the number of squads to create. (default = 1)
         * @param sharedTribe Tribe the squads shared tribe. (default = UNASSIGNED_TRIBE)
         * @param membersAmount Int the number of members per squad to create.
         * @return List<Squad>
         */
        fun create(
            amount: Int,
            membersAmount: Int = 0,
            sharedTribe: Tribe = UNASSIGNED_TRIBE
        ): MutableList<Squad> {
            return (0 until amount).map { this.create(membersAmount, sharedTribe) }.toMutableList()
        }
    }
}
