package app.choppa.integration.domain.rotation

import app.choppa.domain.account.Account
import app.choppa.domain.account.AccountService
import app.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE
import app.choppa.domain.rotation.RotationOptions
import app.choppa.domain.rotation.RotationOptions.Companion.DEFAULT_OPTIONS
import app.choppa.domain.rotation.RotationService
import app.choppa.domain.rotation.filter.Filter.OLDEST
import app.choppa.domain.rotation.strategy.Strategy.CLOCKWISE
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.TribeService
import app.choppa.support.factory.SquadFactory
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.testcontainers.TestDBContainer
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
internal class RotationServiceIT @Autowired constructor(
    private val tribeService: TribeService,
    private val squadService: SquadService,
    private val rotationService: RotationService,
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
    fun `Given existing tribe with no members, when service rotate tribe, then rotationService should not make changes to the tribe`() {
        val tribe = tribeService.save(Tribe())
        val result = rotationService.executeRotation(tribe, DEFAULT_OPTIONS)

        result shouldBeEqualTo tribeService.find(tribe.id)
    }

    @Test
    @Transactional
    fun `Given existing tribe with related squads but no members, when rotationService rotate tribe, then rotationService should not make changes to the tribe`() {
        val tribe = tribeService.save(Tribe()).apply {
            squadService.save(SquadFactory.create(amount = 3, sharedTribe = this))
        }
        val result = rotationService.executeRotation(tribe, DEFAULT_OPTIONS)

        result shouldBeEqualTo tribeService.find(tribe.id)
    }

    @Test
    @Transactional
    fun `Given existing tribe with related squads and members, when rotationService rotate tribe, then rotationService rotate members`() {
        val tribe = tribeService.save(Tribe()).apply {
            this.squads.addAll(
                squadService.save(SquadFactory.create(amount = 3, membersAmount = 1, sharedTribe = this))
            )
        }

        val relatedMembers = tribe.squads.flatMap { it.members }.map { it.name }

        tribeService.find(tribe.id).squads.size shouldBeEqualTo tribe.squads.size

        squadService.findRelatedByTribe(tribe.id).forEachIndexed { index, squad ->
            squad.members.first() shouldBeEqualTo tribe.squads[index].members.first()
        }

        val result = rotationService.executeRotation(
            tribe,
            RotationOptions(tribe.squads.size, UNASSIGNED_ROLE, OLDEST, CLOCKWISE)
        )

        result.squads[0] shouldBeEqualTo tribe.squads[0]
        result.squads[1] shouldBeEqualTo tribe.squads[1]
        result.squads[2] shouldBeEqualTo tribe.squads[2]

        result.squads[0].members.first().name shouldBeEqualTo relatedMembers[2]
        result.squads[1].members.first().name shouldBeEqualTo relatedMembers[0]
        result.squads[2].members.first().name shouldBeEqualTo relatedMembers[1]
    }
}
