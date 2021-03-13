package app.choppa.integration.domain.tribe

import app.choppa.domain.account.Account.Companion.UNASSIGNED_ACCOUNT
import app.choppa.domain.account.AccountService
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.TribeService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.factory.TribeFactory
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
internal class TribeServiceCollectionIT @Autowired constructor(
    private val tribeService: TribeService,
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @MockkBean(relaxed = true)
    private lateinit var accountService: AccountService

    @BeforeEach
    internal fun setUp() {
        every { accountService.resolveFromAuth() } returns UNASSIGNED_ACCOUNT
    }

    @Test
    @Transactional
    fun `Given a new list of tribes, when service saves said list of tribes, then service should persist the list of tribes`() {
        val newListOfTribes = TribeFactory.create(amount = 3)
        val result = tribeService.save(newListOfTribes)

        assertThat(result, List<Tribe>::containsInAnyOrder, newListOfTribes)
    }

    @Test
    @Transactional
    fun `Given an existing list of tribes, when service updates said list of tribes, then service should persist the changed list of tribes`() {
        val existingListOfTribes = tribeService.save(TribeFactory.create(amount = 3))
        val redColor = "#FF0000".toRGBAInt()

        val updatedListOfTribes = existingListOfTribes.map {
            Tribe(it.id, it.name, redColor)
        }

        val result = tribeService.save(updatedListOfTribes)

        assertThat(result, List<Tribe>::containsInAnyOrder, updatedListOfTribes)
    }

    @Test
    @Transactional
    fun `Given an existing list of tribes, when service deletes said list of tribes, then service should remove the existing list of tribes`() {
        val existingListOfTribes = tribeService.save(TribeFactory.create(amount = 3))
        val removedListOfTribes = tribeService.delete(existingListOfTribes)

        assertThrows(EntityNotFoundException::class.java) { tribeService.find(removedListOfTribes.map { it.id }) }
    }
}
