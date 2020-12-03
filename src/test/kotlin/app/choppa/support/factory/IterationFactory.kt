package app.choppa.support.factory

import app.choppa.domain.iteration.Iteration

class IterationFactory {
    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        /**
         * Creates a random iteration.
         *
         * @return Iteration
         */
        fun create(): Iteration = Iteration()

        /**
         * Creates X amount of random iterations
         * @param amount Int the number of iterations to create.
         * @return MutableList<Iteration>
         */
        fun create(amount: Int): MutableList<Iteration> {
            return (0 until amount).map { this.create() }.toMutableList()
        }
    }
}
