package app.choppa.acceptance.domain.squad.history

import app.choppa.domain.squad.history.SquadMemberHistoryRepository
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.factory.SquadMemberHistoryFactory
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SquadMemberHistoryServiceTest {
    private lateinit var repository: SquadMemberHistoryRepository
    private lateinit var service: SquadMemberHistoryService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(SquadMemberHistoryRepository::class)
        service = SquadMemberHistoryService(repository)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = SquadMemberHistoryFactory.create()

        every { repository.save(entity) } returns entity

        val savedEntity = service.save(entity)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun `Given a no entity records, when service tries to find all records, then service should throw EntityNotFoundException`() {
        every { repository.findAll() } returns emptyList()

        assertThrows(EntityNotFoundException::class.java) { service.find() }
    }
}
