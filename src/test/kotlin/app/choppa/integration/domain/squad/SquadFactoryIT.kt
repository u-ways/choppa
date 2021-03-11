package app.choppa.integration.domain.squad

import app.choppa.domain.account.Account.Companion.UNASSIGNED_ACCOUNT
import app.choppa.domain.member.MemberFactory
import app.choppa.domain.squad.SquadFactory
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.Tribe.Companion.UNASSIGNED_TRIBE
import app.choppa.domain.tribe.TribeService
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.testcontainers.TestDBContainer
import app.choppa.utils.Color.Companion.toRGBAInt
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.UUID.randomUUID
import javax.transaction.Transactional

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class SquadFactoryIT @Autowired constructor(
    private val squadFactory: SquadFactory,
    private val memberFactory: MemberFactory,
    private val squadService: SquadService,
    private val tribeService: TribeService,
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun `Given new squad details, when factory creates the squad, it should store squad in database`() {
        val id = randomUUID()
        val name = "Squad Test"
        val color = "#FFFFFF".toRGBAInt()
        val members = memberFactory.create(List(3) { "MEM-$it" }, sharedAccount = UNASSIGNED_ACCOUNT)

        squadFactory.create(id, name, color, UNASSIGNED_TRIBE, members, UNASSIGNED_ACCOUNT)

        val expectedSquad = squadService.find(id, UNASSIGNED_ACCOUNT)

        expectedSquad.id shouldBe id
        expectedSquad.name shouldBe name
        expectedSquad.color shouldBe color
        expectedSquad.members shouldContainAll members
    }

    @Test
    @Transactional
    fun `Given several squad details, when factory creates the squads, it should store squads in database`() {
        val squads = listOf(
            Triple("Alfa", "#FF0000".toRGBAInt(), memberFactory.create(List(3) { "MEM-$it" })),
            Triple("Bravo", "#00FF00".toRGBAInt(), memberFactory.create(List(1) { "Member Test" })),
            Triple("Charlie", "#0000FF".toRGBAInt(), memberFactory.create(emptyList())),
        )
        val sharedTribe = tribeService.save(Tribe(), UNASSIGNED_ACCOUNT)
        val sharedAccount = UNASSIGNED_ACCOUNT

        squadFactory.create(squads, sharedTribe, sharedAccount)

        val expectedSquads = squadService.findRelatedByTribe(sharedTribe.id, sharedAccount)

        expectedSquads.size shouldBe squads.size

        expectedSquads.map { it.name } shouldContainAll squads.map { it.first }
        expectedSquads.map { it.color } shouldContainAll squads.map { it.second }
        expectedSquads.map { it.members } shouldContainAll squads.map { it.third }
    }
}
