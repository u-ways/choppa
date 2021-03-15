package app.choppa.integration.domain.rotation

import app.choppa.domain.rotation.RotationOptions
import app.choppa.domain.rotation.RotationService
import app.choppa.domain.rotation.filter.Filter.OLDEST
import app.choppa.domain.rotation.strategy.Strategy.CLOCKWISE
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.TribeService
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import app.choppa.support.factory.TribeFactory
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

internal class RotationServiceIT @Autowired constructor(
    private val tribeService: TribeService,
    private val squadService: SquadService,
    private val rotationService: RotationService,
) : BaseServiceIT() {

    @Test
    @Transactional
    fun `Given existing tribe with no members, when service rotate tribe, then rotationService should not make changes to the tribe`() {
        val tribe = tribeService.save(TribeFactory.create())
        val result = rotationService.executeRotation(tribe, RotationOptions(chapter = CHAPTER))

        result shouldBeEqualTo tribeService.find(tribe.id)
    }

    @Test
    @Transactional
    fun `Given existing tribe with related squads but no members, when rotationService rotate tribe, then rotationService should not make changes to the tribe`() {
        val tribe = tribeService.save(TribeFactory.create()).apply {
            squadService.save(SquadFactory.create(amount = 3, sharedTribe = this))
        }
        val result = rotationService.executeRotation(tribe, RotationOptions(chapter = CHAPTER))

        result shouldBeEqualTo tribeService.find(tribe.id)
    }

    @Test
    @Transactional
    fun `Given existing tribe with related squads and members, when rotationService rotate tribe, then rotationService rotate members`() {
        val tribe = tribeService.save(TribeFactory.create()).apply {
            this.squads.addAll(
                squadService.save(
                    SquadFactory.create(amount = 3, sharedTribe = this).onEach {
                        it.members.add(MemberFactory.create())
                    }
                )
            )
        }

        val relatedMembers = tribe.squads.flatMap { it.members }.map { it.name }

        tribeService.find(tribe.id).squads.size shouldBeEqualTo tribe.squads.size

        squadService.findRelatedByTribe(tribe.id).forEachIndexed { index, squad ->
            squad.members.first() shouldBeEqualTo tribe.squads[index].members.first()
        }

        val result = rotationService.executeRotation(
            tribe,
            RotationOptions(tribe.squads.size, CHAPTER, OLDEST, CLOCKWISE)
        )

        result.squads[0] shouldBeEqualTo tribe.squads[0]
        result.squads[1] shouldBeEqualTo tribe.squads[1]
        result.squads[2] shouldBeEqualTo tribe.squads[2]

        result.squads[0].members.first().name shouldBeEqualTo relatedMembers[2]
        result.squads[1].members.first().name shouldBeEqualTo relatedMembers[0]
        result.squads[2].members.first().name shouldBeEqualTo relatedMembers[1]
    }
}
