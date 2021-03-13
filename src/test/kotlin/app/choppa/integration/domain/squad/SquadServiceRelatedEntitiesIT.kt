package app.choppa.integration.domain.squad

import app.choppa.domain.account.Account
import app.choppa.domain.account.AccountService
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.TribeService
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.matchers.containsInAnyOrder
import app.choppa.support.testcontainers.TestDBContainer
import com.natpryce.hamkrest.assertion.assertThat
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
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

    @MockkBean(relaxed = true)
    private lateinit var accountService: AccountService

    @BeforeEach
    internal fun setUp() {
        every { accountService.resolveFromAuth() } returns Account.UNASSIGNED_ACCOUNT
    }

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
            listOf(
                Squad(members = members.toMutableList()),
                Squad(members = members.toMutableList()),
                Squad(members = members.toMutableList())
            )
        )

        val actualRelatedMemberSquads = squadService.findRelatedByMember(members.first().id)

        assertThat(actualRelatedMemberSquads, List<Squad>::containsInAnyOrder, expectedRelatedMemberSquads)
    }
}
