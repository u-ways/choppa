package org.choppa.support.factory

import org.choppa.model.squad.Squad

class SquadFactory {
    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        /**
         * Creates a random Squad with X amount of members.
         *
         * @param membersAmount Int the number of members to the squad has.
         * @return Squad
         */
        fun create(
            membersAmount: Int = 0
        ): Squad = Squad(members = MemberFactory.create(membersAmount))

        /**
         * Create X amount of squads with Y amount of members in each.
         *
         * @param amount Int the number of squads to create. (default = 1)
         * @param membersAmount Int the number of members per squad to create.
         * @return List<Squad>
         */
        fun create(
            amount: Int,
            membersAmount: Int = 0
        ): MutableList<Squad> {
            return (0 until amount).map { this.create(membersAmount) }.toMutableList()
        }
    }
}
