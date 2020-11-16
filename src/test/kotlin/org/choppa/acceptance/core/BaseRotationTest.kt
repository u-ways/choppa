package org.choppa.acceptance.core

import org.choppa.core.BaseRotation
import org.choppa.domain.chapter.Chapter
import org.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE
import org.choppa.support.factory.TribeFactory
import org.junit.jupiter.api.Test

class BaseRotationTest {
    @Test
    internal fun `Given Tribe with no members, when tribe is rotated, it should not do any changes`() {
        val testTribe = TribeFactory.create()
        val rotatedTribe = BaseRotation.rotate(testTribe, Chapter(), clockWise = true)
        assert(testTribe === rotatedTribe)
    }

    @Test
    internal fun `Given Tribe with 2 squads with 1 member each, when tribe rotates, members should swap squads`() {
        val testTribe = TribeFactory.create(2, 1)
        val rotatedTribe = BaseRotation.rotate(testTribe, UNASSIGNED_ROLE, clockWise = true)
        assert(testTribe.squads[0].members[0] === rotatedTribe.squads[1].members[0])
        assert(testTribe.squads[1].members[0] === rotatedTribe.squads[0].members[0])
    }

    @Test
    internal fun `Given Tribe with 3 squads with 2 members each, when tribe rotates clockwise, longest held members should swap squads`() {
        val testTribe = TribeFactory.create(3, 2)
        val oldestMemberSquadOne = testTribe.squads[0].members[0]
        val oldestMemberSquadTwo = testTribe.squads[1].members[0]
        val oldestMemberSquadThree = testTribe.squads[2].members[0]
        val rotatedTribe = BaseRotation.rotate(testTribe, UNASSIGNED_ROLE, clockWise = true)

        assert(rotatedTribe.squads[0].members.contains(oldestMemberSquadThree))
        assert(rotatedTribe.squads[1].members.contains(oldestMemberSquadOne))
        assert(rotatedTribe.squads[2].members.contains(oldestMemberSquadTwo))
    }

    @Test
    internal fun `Given Tribe with 3 squads with 2 members each, when tribe rotates counter-clockwise, longest held members should swap squads`() {
        val testTribe = TribeFactory.create(3, 2)
        val oldestMemberSquadOne = testTribe.squads[0].members[0]
        val oldestMemberSquadTwo = testTribe.squads[1].members[0]
        val oldestMemberSquadThree = testTribe.squads[2].members[0]
        val rotatedTribe = BaseRotation.rotate(testTribe, UNASSIGNED_ROLE, clockWise = false)

        assert(rotatedTribe.squads[0].members.contains(oldestMemberSquadTwo))
        assert(rotatedTribe.squads[1].members.contains(oldestMemberSquadThree))
        assert(rotatedTribe.squads[2].members.contains(oldestMemberSquadOne))
    }

    @Test
    internal fun `Given Tribe with 3 squads with 2 members each, when tribe rotates randomly, squad compositions should change`() {
        val testTribe = TribeFactory.create(3, 2)
        val rotatedTribe = BaseRotation.randomRotate(testTribe, UNASSIGNED_ROLE)

        val uniquesSquadOne =
            testTribe.squads[0].members.filter { member -> rotatedTribe.squads[0].members.contains(member) }.count()
        val uniquesSquadTwo =
            testTribe.squads[1].members.filter { member -> rotatedTribe.squads[1].members.contains(member) }.count()
        val uniquesSquadThree =
            testTribe.squads[2].members.filter { member -> rotatedTribe.squads[2].members.contains(member) }.count()

        assert(uniquesSquadOne == 1)
        assert(uniquesSquadTwo == 1)
        assert(uniquesSquadThree == 1)
        assert(testTribe.squads[0].members.count() == 2)
        assert(testTribe.squads[1].members.count() == 2)
        assert(testTribe.squads[2].members.count() == 2)
    }
}
