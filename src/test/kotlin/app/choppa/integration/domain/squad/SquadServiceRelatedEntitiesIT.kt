package app.choppa.integration.domain.squad

import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.TribeService
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import app.choppa.support.factory.TribeFactory
import app.choppa.support.matchers.containsInAnyOrder
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

internal class SquadServiceRelatedEntitiesIT @Autowired constructor(
    private val memberService: MemberService,
    private val squadService: SquadService,
    private val tribeService: TribeService,
) : BaseServiceIT() {

    @Test
    @Transactional
    fun `Given tribe with squads, when service find squads by related tribe, then service should return related squads`() {
        squadService.save(SquadFactory.create(amount = 2)) // random squads

        val relatedTribe = tribeService.save(TribeFactory.create())
        val expectedRelatedTribeSquads = squadService.save(SquadFactory.create(amount = 2, sharedTribe = relatedTribe))

        val actualRelatedTribeSquads = squadService.findRelatedByTribe(relatedTribe.id)

        assertThat(actualRelatedTribeSquads, List<Squad>::containsInAnyOrder, expectedRelatedTribeSquads)
    }

    @Test
    @Transactional
    fun `Given member in squads, when service find squads by related member, then service should return related squads`() {
        squadService.save(SquadFactory.create(amount = 2)) // random squads

        val relatedMembers = memberService.save(MemberFactory.create(2))
        val expectedRelatedMemberSquads = squadService.save(
            listOf(
                SquadFactory.create(members = relatedMembers.toMutableList()),
                SquadFactory.create(members = relatedMembers.toMutableList()),
            )
        )

        val actualRelatedMemberSquads = squadService.findRelatedByMember(relatedMembers.first().id)

        assertThat(actualRelatedMemberSquads, List<Squad>::containsInAnyOrder, expectedRelatedMemberSquads)
    }
}
