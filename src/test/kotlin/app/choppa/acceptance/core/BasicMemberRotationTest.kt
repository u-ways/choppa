package app.choppa.acceptance.core

import app.choppa.core.rotation.Options
import app.choppa.core.rotation.Rotation
import app.choppa.core.rotation.filter.Filter.NONE
import app.choppa.core.rotation.filter.Filter.OLDEST
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
        val rotatedTribe = Rotation.rotate(testTribe, Options(1, UNASSIGNED_ROLE, OLDEST, CLOCKWISE))
        assert(testTribe === rotatedTribe)
    }

    @Test
    fun `Given Tribe with 2 squads with 1 member each, when tribe rotates, members should swap squads`() {
        val testTribe = TribeFactory.create(2, 1)
        val rotatedTribe = Rotation.rotate(testTribe, Options(1, UNASSIGNED_ROLE, OLDEST, CLOCKWISE))
        assert(testTribe.squads[0].members[0] === rotatedTribe.squads[1].members[0])
        assert(testTribe.squads[1].members[0] === rotatedTribe.squads[0].members[0])
    }

    @Test
    fun `Given Tribe with 3 squads with 2 members each, when tribe rotates clockwise, longest held members should swap squads`() {
        val testTribe = TribeFactory.create(3, 2)
        val oldestMemberSquadOne = testTribe.squads[0].members[0]
        val oldestMemberSquadTwo = testTribe.squads[1].members[0]
        val oldestMemberSquadThree = testTribe.squads[2].members[0]
        val rotatedTribe = Rotation.rotate(testTribe, Options(1, UNASSIGNED_ROLE, OLDEST, CLOCKWISE))

        assert(rotatedTribe.squads[0].members.contains(oldestMemberSquadThree))
        assert(rotatedTribe.squads[1].members.contains(oldestMemberSquadOne))
        assert(rotatedTribe.squads[2].members.contains(oldestMemberSquadTwo))
    }

    @Test
    fun `Given Tribe with 3 squads with 2 members each, when tribe rotates counter-clockwise, longest held members should swap squads`() {
        val testTribe = TribeFactory.create(3, 2)
        val oldestMemberSquadOne = testTribe.squads[0].members[0]
        val oldestMemberSquadTwo = testTribe.squads[1].members[0]
        val oldestMemberSquadThree = testTribe.squads[2].members[0]
        val rotatedTribe = Rotation.rotate(testTribe, Options(1, UNASSIGNED_ROLE, OLDEST, ANTI_CLOCKWISE))

        assert(rotatedTribe.squads[0].members.contains(oldestMemberSquadTwo))
        assert(rotatedTribe.squads[1].members.contains(oldestMemberSquadThree))
        assert(rotatedTribe.squads[2].members.contains(oldestMemberSquadOne))
    }

    @ParameterizedTest(name = "Given Tribe with {0} squads with {1} members each, when tribe rotates {2} members randomly, squad compositions should change by an equal amount.")
    @MethodSource("randomArguments")
    fun `Given Tribe with X squads with Y members each, when tribe rotates Z members randomly, squad compositions should change by an equal amount`(
        squadAmount: Int,
        squadMemberAmount: Int,
        squadRotationAmount: Int
    ) {
        val testTribe = TribeFactory.create(squadAmount, squadMemberAmount)
        val rotatedTribe = Rotation.rotate(testTribe, Options(squadRotationAmount, UNASSIGNED_ROLE, NONE, RANDOM))

        val distinctRotatedMemberCounts = testTribe.squads.mapIndexed { index, squad ->
            squad.members.filter { !rotatedTribe.squads[index].members.contains(it) }.count()
        }.distinct()

        assert(distinctRotatedMemberCounts.count() == 1)
        assert(distinctRotatedMemberCounts[0] == squadRotationAmount)
    }

    companion object {
        @JvmStatic
        fun randomArguments() = Stream.of(
            Arguments.of(2, 2, 1),
            Arguments.of(3, 2, 1),
            Arguments.of(5, 5, 5),
            Arguments.of(10, 20, 15)
        )
    }
}
