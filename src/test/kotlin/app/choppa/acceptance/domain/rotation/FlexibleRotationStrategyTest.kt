package app.choppa.acceptance.domain.rotation

import app.choppa.domain.member.Member
import app.choppa.domain.rotation.RotationContext.Companion.rotate
import app.choppa.domain.rotation.RotationOptions
import app.choppa.domain.rotation.filter.Filter
import app.choppa.domain.rotation.filter.Filter.DISTRIBUTED
import app.choppa.domain.rotation.filter.Filter.OLDEST
import app.choppa.domain.rotation.strategy.Strategy
import app.choppa.domain.rotation.strategy.Strategy.*
import app.choppa.domain.squad.Squad
import app.choppa.support.base.Universe
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import app.choppa.support.factory.TribeFactory
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class FlexibleRotationStrategyTest : Universe() {
    @Test
    fun `Given Tribe with 2 squads with older members, when tribe rotates by oldest, then only older members should rotate`() {
        /**
         * A tribe's revisions list consist of its members formation revisions.
         * It's a list of squads paired their complete members formation revision snapshots.
         *
         * Below is an example of tribe's two squads with 5 revision each:
         * - index 0 => Revision 0 [Member 1]           (first member added)
         * - index 1 => Revision 1 [Member 1, Member 2] (another member added)
         * - index 2 => Revision 2 [Member 2]           (member 1 removed)
         * - index 3 => Revision 3 [Member 2, Member 3] (member 3 added)
         * - index 4 => Revision 4 [Member 2, Member 1] (member 3 removed, member 1 added again, making member 2 the oldest member)
         */
        val revisions: List<Pair<Squad, List<List<Member>>>> = SquadFactory.create(2)
            .onEach { it.members.addAll(MemberFactory.create(2)) }.map {
                it to listOf(
                    listOf(it.members.first()),
                    listOf(it.members.first(), it.members.last()),
                    listOf(it.members.last()),
                    listOf(it.members.last(), MemberFactory.create()),
                    listOf(it.members.last(), it.members.first())
                ).apply {
                    // make squad current members list have the same order as latest revision.
                    it.members.reverse()
                }
            }

        val tribe = TribeFactory.create(squads = revisions.map { it.first }.toMutableList())
        val result = rotate(tribe, RotationOptions(amount = 2, CHAPTER, OLDEST, CLOCKWISE), revisions)

        result.squads.forEachIndexed { index, squad ->
            squad.members.first() shouldBeEqualTo tribe.squads[index].members.last()
            squad.members.size shouldBeEqualTo tribe.squads[index].members.size
        }
    }

    @Test
    fun `Given Tribe with 3 squads with 2 members each, when filtering oldest and rotating three members counter-clockwise, longest held members should swap squads`() {
        val testTribe = TribeFactory.create(
            squads = SquadFactory.create(3)
                .onEach { it.members.addAll(MemberFactory.create(2)) }
        )

        val oldestMemberOne = testTribe.squads[0].members[0]
        val oldestMemberTwo = testTribe.squads[1].members[0]
        val oldestMemberThree = testTribe.squads[2].members[0]

        val rotatedTribe = rotate(testTribe, RotationOptions(3, CHAPTER, OLDEST, ANTI_CLOCKWISE))

        rotatedTribe.squads[0].members[1] shouldBeEqualTo oldestMemberTwo
        rotatedTribe.squads[1].members[1] shouldBeEqualTo oldestMemberThree
        rotatedTribe.squads[2].members[1] shouldBeEqualTo oldestMemberOne
    }

    @Test
    fun `Given Tribe with 3 squads with older members, when tribe rotates by oldest and anti-clockwise, then only older members should rotate`() {
        val revisions = SquadFactory.create(2)
            .onEach { it.members.addAll(MemberFactory.create(2)) }.map {
                it to listOf(
                    listOf(it.members.first()),
                    listOf(it.members.first(), it.members.last()),
                    listOf(it.members.last()),
                    listOf(it.members.last(), MemberFactory.create()),
                    listOf(it.members.last(), it.members.first())
                ).apply {
                    // make squad current members list have the same order as latest revision.
                    it.members.reverse()
                }
            }.plus(
                SquadFactory.create()
                    .apply { members.add(MemberFactory.create()) }
                    .run { this to listOf(listOf(this.members.first())) }
            )

        val tribe = TribeFactory.create(squads = revisions.map { it.first }.toMutableList())
        val result = rotate(tribe, RotationOptions(amount = 3, CHAPTER, OLDEST, CLOCKWISE), revisions)

        result.squads.forEachIndexed { index, squad ->
            squad.members.size shouldBeEqualTo tribe.squads[index].members.size
        }

        result.squads[0].members[1] shouldBeEqualTo tribe.squads[2].members[0]
        result.squads[1].members[1] shouldBeEqualTo tribe.squads[0].members[0]
        result.squads[2].members[0] shouldBeEqualTo tribe.squads[1].members[0]
    }

    @ParameterizedTest(name = "Given Tribe with 2 squads with 1 member each, when tribe filters {0} and rotates {1}, members should swap squads")
    @MethodSource("filtersAndClockDirection")
    fun `Given Tribe with 2 squads with 1 member each, when tribe filters X and rotates two members Y, all members should swap squads`(
        filter: Filter,
        strategy: Strategy
    ) {
        val testTribe = TribeFactory.create(
            squads = SquadFactory.create(2)
                .onEach { it.members.addAll(MemberFactory.create(1)) }
        )

        val rotatedTribe = rotate(testTribe, RotationOptions(2, CHAPTER, filter, strategy))
        assert(testTribe.squads[0].members[0] === rotatedTribe.squads[1].members[0])
        assert(testTribe.squads[1].members[0] === rotatedTribe.squads[0].members[0])
    }

    @Test
    fun `Given Tribe with 3 squads with 2 members each, when filtering distributed and rotating three members clockwise, the top member from each squad should swap squads`() {
        val testTribe = TribeFactory.create(
            squads = SquadFactory.create(3)
                .onEach { it.members.addAll(MemberFactory.create(2)) }
        )

        val topMemberOne = testTribe.squads[0].members[0]
        val topMemberTwo = testTribe.squads[1].members[0]
        val topMemberThree = testTribe.squads[2].members[0]
        val rotatedTribe = rotate(testTribe, RotationOptions(3, CHAPTER, DISTRIBUTED, CLOCKWISE))

        assert(rotatedTribe.squads[0].members.contains(topMemberThree))
        assert(rotatedTribe.squads[1].members.contains(topMemberOne))
        assert(rotatedTribe.squads[2].members.contains(topMemberTwo))
    }

    @ParameterizedTest(name = "Given Tribe with {0} squads with {1} members each, when tribe rotates {2} members randomly, squad compositions should change by an equal amount.")
    @MethodSource("randomArguments")
    fun `Given Tribe with X squads with Y members each, when tribe rotates Z members randomly, the diff in members between teams should be identical to the rotation amount`(
        squadAmount: Int,
        squadMemberAmount: Int,
        squadRotationAmount: Int
    ) {
        val testTribe = TribeFactory.create(
            squads = SquadFactory.create(squadAmount)
                .onEach { it.members.addAll(MemberFactory.create(squadMemberAmount)) }
        )

        val rotatedTribe =
            rotate(testTribe, RotationOptions(squadRotationAmount, CHAPTER, Filter.RANDOM, RANDOM))

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
