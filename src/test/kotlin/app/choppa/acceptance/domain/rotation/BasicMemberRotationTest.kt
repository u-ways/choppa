package app.choppa.acceptance.domain.rotation

import app.choppa.domain.rotation.RotationContext.Companion.rotate
import app.choppa.domain.rotation.RotationOptions
import app.choppa.domain.rotation.filter.Filter.OLDEST
import app.choppa.domain.rotation.strategy.Strategy.CLOCKWISE
import app.choppa.support.base.Universe
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import app.choppa.support.factory.TribeFactory
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class BasicMemberRotationTest : Universe() {
    @Test
    fun `Given Tribe with no members, when tribe is rotated, it should not do any changes`() {
        val testTribe = TribeFactory.create()
        val rotatedTribe = rotate(testTribe, RotationOptions(1, CHAPTER, OLDEST, CLOCKWISE))
        assert(testTribe === rotatedTribe)
    }

    @Test
    fun `Given Tribe with 2 squads with 1 members each, when rotating members clockwise, members should swap squads`() {
        val testTribe = TribeFactory.create(
            squads = SquadFactory.create(2)
                .onEach { it.members.add(MemberFactory.create()) }
        )
        val rotatedTribe = rotate(testTribe, RotationOptions(2, CHAPTER, OLDEST, CLOCKWISE))

        rotatedTribe.squads[0].members.first() shouldBeEqualTo testTribe.squads[1].members.first()
        rotatedTribe.squads[1].members.first() shouldBeEqualTo testTribe.squads[0].members.first()
    }

    @Test
    fun `Given Tribe with 3 squads with 2 members each, when rotating three members clockwise, first held members should rotate`() {
        val testTribe = TribeFactory.create(
            squads = SquadFactory.create(3)
                .onEach { it.members.addAll(MemberFactory.create(2)) }
        )
        val rotatedTribe = rotate(testTribe, RotationOptions(3, CHAPTER, OLDEST, CLOCKWISE))

        rotatedTribe.squads[0].members[1] shouldBeEqualTo testTribe.squads[2].members.first()
        rotatedTribe.squads[1].members[1] shouldBeEqualTo testTribe.squads[0].members.first()
        rotatedTribe.squads[2].members[1] shouldBeEqualTo testTribe.squads[1].members.first()
    }
}
