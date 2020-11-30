package app.choppa.support.factory

import app.choppa.domain.history.History
import app.choppa.domain.iteration.Iteration
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import java.time.Instant.now

class HistoryFactory {
    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        /**
         * Create a random history record.
         *
         * @return History
         */
        fun create(): History = History(Iteration(), Tribe(), Squad(), Member())

        /**
         * Creates X amount of history records with no relation at all.
         *
         * @param amount Int
         * @return MutableList<History>
         */
        fun create(amount: Int): MutableList<History> {
            return (0 until amount).map { this.create() }.toMutableList()
        }

        /**
         * Creates a history snapshot of a given tribe and current iteration.
         * If no iteration passed, the function will generate the next iteration number
         * based on tribe's previous iteration number.
         *
         * NOTE: This will create a side-effect on all history related domain entities
         *       for the given tribe. (i.e. tribe, squads, members will have a new
         *       history record entry in their history mutable list.
         *
         * @param tribe Tribe
         * @param currentIteration Iteration
         * @return MutableList<History>
         */
        fun create(
            tribe: Tribe,
            currentIteration: Iteration = Iteration(
                number = if (tribe.history.isEmpty()) 1 else tribe.history.last().iteration.number + 1
            )
        ): MutableList<History> = tribe.apply {
            val createDate = now()
            this.squads.forEach { squad ->
                squad.members.forEach { member ->
                    History(currentIteration, tribe, squad, member, createDate).apply {
                        currentIteration.history.add(this)
                        tribe.history.add(this)
                        squad.history.add(this)
                        member.history.add(this)
                    }
                }
            }
        }.history
    }
}
