package app.choppa.integration.domain.squad

import app.choppa.domain.account.Account
import app.choppa.domain.account.AccountService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.factory.SquadFactory
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.matchers.containsInAnyOrder
import app.choppa.support.testcontainers.TestDBContainer
import app.choppa.utils.Color.Companion.toRGBAInt
import com.natpryce.hamkrest.assertion.assertThat
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertThrows
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
internal class SquadServiceCollectionIT @Autowired constructor(
    private val squadService: SquadService,
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
    fun `Given a new list of squads, when service saves said list of squads, then service should persist the list of squads`() {
        val newListOfSquads = SquadFactory.create(amount = 3)
        val result = squadService.save(newListOfSquads)

        assertThat(result, List<Squad>::containsInAnyOrder, newListOfSquads)
    }

    @Test
    @Transactional
    fun `Given an existing list of squads, when service updates said list of squads, then service should persist the changed list of squads`() {
        val existingListOfSquads = squadService.save(SquadFactory.create(amount = 3))
        val redColor = "#FF0000".toRGBAInt()

        val updatedListOfSquads = existingListOfSquads.map {
            Squad(it.id, it.name, redColor)
        }

        val result = squadService.save(updatedListOfSquads)

        assertThat(result, List<Squad>::containsInAnyOrder, updatedListOfSquads)
    }

    @Test
    @Transactional
    fun `Given an existing list of squads, when service deletes said list of squads, then service should remove the existing list of squads`() {
        val existingListOfSquads = squadService.save(SquadFactory.create(amount = 3))
        val removedListOfSquads = squadService.delete(existingListOfSquads)

        assertThrows(EntityNotFoundException::class.java) { squadService.find(removedListOfSquads.map { it.id }) }
    }
}
