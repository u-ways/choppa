package app.choppa.acceptance.core

import app.choppa.core.rotation.Context
import app.choppa.domain.rotation.RotationOptions
import app.choppa.core.rotation.filter.Filter
import app.choppa.core.rotation.filter.Filter.DISTRIBUTED
import app.choppa.core.rotation.filter.Filter.OLDEST
import app.choppa.core.rotation.strategy.Strategy
import app.choppa.core.rotation.strategy.Strategy.ANTI_CLOCKWISE
import app.choppa.core.rotation.strategy.Strategy.CLOCKWISE
import app.choppa.core.rotation.strategy.Strategy.RANDOM
import app.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE
import app.choppa.support.factory.TribeFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class BasicMemberRotationTest {
    @Test
    fun `Given Tribe with no members, when tribe is rotated, it should not do any changes`() {
        val testTribe = TribeFactory.create()
        val rotatedTribe = Context.rotate(testTribe, RotationOptions(1, UNASSIGNED_ROLE, OLDEST, CLOCKWISE))
        assert(testTribe === rotatedTribe)
    }

    @ParameterizedTest(name = "Given Tribe with 2 squads with 1 member each, when tribe filters {0} and rotates {1}, members should swap squads")
    @MethodSource("filtersAndClockDirection")
    fun `Given Tribe with 2 squads with 1 member each, when tribe filters X and rotates two members Y, all members should swap squads`(
        filter: Filter,
        strategy: Strategy
    ) {
        val testTribe = TribeFactory.create(2, 1)
        val rotatedTribe = Context.rotate(testTribe, RotationOptions(2, UNASSIGNED_ROLE, filter, strategy))
        assert(testTribe.squads[0].members[0] === rotatedTribe.squads[1].members[0])
        assert(testTribe.squads[1].members[0] === rotatedTribe.squads[0].members[0])
    }

    @Test
    fun `Given Tribe with 3 squads with 2 members each, when filtering distributed and rotating three members clockwise, the top member from each squad should swap squads`() {
        val testTribe = TribeFactory.create(3, 2)
        val topMemberOne = testTribe.squads[0].members[0]
        val topMemberTwo = testTribe.squads[1].members[0]
        val topMemberThree = testTribe.squads[2].members[0]
        val rotatedTribe = Context.rotate(testTribe, RotationOptions(3, UNASSIGNED_ROLE, DISTRIBUTED, CLOCKWISE))

        assert(rotatedTribe.squads[0].members.contains(topMemberThree))
        assert(rotatedTribe.squads[1].members.contains(topMemberOne))
        assert(rotatedTribe.squads[2].members.contains(topMemberTwo))
    }

    @Test
    fun `Given Tribe with 3 squads with 2 members each, when filtering oldest and rotating three members counter-clockwise, longest held members should swap squads`() {
        val testTribe = TribeFactory.create(3, 2)
        val oldestMemberOne = testTribe.squads[0].members[1]
        val oldestMemberTwo = testTribe.squads[0].members[0]
        val oldestMemberThree = testTribe.squads[1].members[0]
        val rotatedTribe = Context.rotate(testTribe, RotationOptions(3, UNASSIGNED_ROLE, OLDEST, ANTI_CLOCKWISE))

        assert(rotatedTribe.squads[2].members.contains(oldestMemberOne))
        assert(rotatedTribe.squads[2].members.contains(oldestMemberTwo))
        assert(rotatedTribe.squads[0].members.contains(oldestMemberThree))
    }

    @ParameterizedTest(name = "Given Tribe with {0} squads with {1} members each, when tribe rotates {2} members randomly, squad compositions should change by an equal amount.")
    @MethodSource("randomArguments")
    fun `Given Tribe with X squads with Y members each, when tribe rotates Z members randomly, the diff in members between teams should be identical to the rotation amount`(
        squadAmount: Int,
        squadMemberAmount: Int,
        squadRotationAmount: Int
    ) {
        val testTribe = TribeFactory.create(squadAmount, squadMemberAmount)
        val rotatedTribe = Context.rotate(testTribe, RotationOptions(squadRotationAmount, UNASSIGNED_ROLE, Filter.RANDOM, RANDOM))

        val rotatedMemberCounts = testTribe.squads.mapIndexed { index, squad ->
            squad.members.filter { !rotatedTribe.squads[index].members.contains(it) }.count()
        }

        assert(rotatedMemberCounts.sum() == squadRotationAmount)
    }

    companion object {
        @JvmStatic
        fun filtersAndClockDirection() = Stream.of(
            Arguments.of(DISTRIBUTED, CLOCKWISE),
            Arguments.of(OLDEST, CLOCKWISE),
            Arguments.of(Filter.RANDOM, CLOCKWISE),
            Arguments.of(DISTRIBUTED, ANTI_CLOCKWISE),
            Arguments.of(OLDEST, ANTI_CLOCKWISE),
            Arguments.of(Filter.RANDOM, ANTI_CLOCKWISE)
        )

        @JvmStatic
        fun randomArguments() = Stream.of(
            Arguments.of(2, 2, 1),
            Arguments.of(3, 2, 1),
            Arguments.of(5, 5, 5),
            Arguments.of(10, 20, 15)
        )
    }
}
