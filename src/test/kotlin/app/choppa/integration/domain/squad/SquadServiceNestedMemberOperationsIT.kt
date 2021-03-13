package app.choppa.integration.domain.squad

import app.choppa.domain.account.Account
import app.choppa.domain.account.AccountService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadService
import app.choppa.support.factory.MemberFactory
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.matchers.containsInAnyOrder
import app.choppa.support.testcontainers.TestDBContainer
import com.natpryce.hamkrest.assertion.assertThat
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.amshove.kluent.shouldBeEqualTo
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
internal class SquadServiceNestedMemberOperationsIT @Autowired constructor(
    private val squadService: SquadService,
    private val memberService: MemberService,
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
    fun `Given new squad with non-existent members, when squad service saves new squad, then squad service should also handle saving the non-existing members`() {
        // Create a new squad with "SquadCurrentMembers" members that do not exist in the members repository.
        val newSquad = Squad(members = MemberFactory.create(2))
        val result = squadService.save(newSquad)

        assertThat(result.members, List<Member>::containsInAnyOrder, newSquad.members)
    }

    @Test
    @Transactional
    fun `Given new squad with existing members, when squad service saves new squad with one new member, then squad service should save the new member and add it to the list of squad current members`() {
        val newSquad = Squad(members = memberService.save(MemberFactory.create(2)).toMutableList())
        val nonExistentMember = Member()

        squadService.save(
            newSquad.apply {
                this.members.add(nonExistentMember)
            }
        )

        val result = memberService.find(nonExistentMember.id)

        result shouldBeEqualTo nonExistentMember
    }
}
