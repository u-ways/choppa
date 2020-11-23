package org.choppa.integration.domain.squad

import com.natpryce.hamkrest.assertion.assertThat
import org.choppa.domain.member.MemberService
import org.choppa.domain.squad.Squad
import org.choppa.domain.squad.SquadService
import org.choppa.domain.tribe.Tribe
import org.choppa.domain.tribe.TribeService
import org.choppa.support.factory.MemberFactory
import org.choppa.support.factory.SquadFactory
import org.choppa.support.flyway.FlywayMigrationConfig
import org.choppa.support.matchers.containsInAnyOrder
import org.choppa.support.testcontainers.TestDBContainer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.transaction.Transactional

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class SquadServiceRelatedEntitiesIT @Autowired constructor(
    private val memberService: MemberService,
    private val squadService: SquadService,
    private val tribeService: TribeService,
) {

    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun `Given tribe with squads, when service find squads by related tribe, then service should return related squads`() {
        squadService.save(SquadFactory.create(amount = 2)) // random squads

        val relatedTribe = tribeService.save(Tribe())
        val expectedRelatedTribeSquads = squadService.save(SquadFactory.create(amount = 2, sharedTribe = relatedTribe))

        val actualRelatedTribeSquads = squadService.findRelatedByTribe(relatedTribe.id)

        assertThat(actualRelatedTribeSquads, List<Squad>::containsInAnyOrder, expectedRelatedTribeSquads)
    }

    @Test
    @Transactional
    fun `Given member in squads, when service find squads by related member, then service should return related squads`() {
        squadService.save(SquadFactory.create(amount = 2)) // random squads

        val members = memberService.save(MemberFactory.create(2))
        val expectedRelatedMemberSquads = squadService.save(
            Squad(members = members.toMutableList()),
            Squad(members = members.toMutableList()),
            Squad(members = members.toMutableList())
        )

        val actualRelatedMemberSquads = squadService.findRelatedByMember(members.first().id)

        assertThat(actualRelatedMemberSquads, List<Squad>::containsInAnyOrder, expectedRelatedMemberSquads)
    }
}
