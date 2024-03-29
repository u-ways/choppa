package app.choppa.acceptance.domain.tribe

import app.choppa.domain.account.AccountService
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.TribeRepository
import app.choppa.domain.tribe.TribeService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.factory.TribeFactory
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional.empty
import java.util.Optional.of
import java.util.UUID.randomUUID

internal class TribeServiceTest {
    private lateinit var repository: TribeRepository
    private lateinit var squadService: SquadService
    private lateinit var accountService: AccountService
    private lateinit var service: TribeService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(TribeRepository::class)
        squadService = mockkClass(SquadService::class)
        accountService = mockkClass(AccountService::class, relaxed = true)
        service = TribeService(repository, squadService, accountService)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = TribeFactory.create()

        every { repository.findById(entity.id) } returns empty()
        every { repository.save(entity) } returns entity

        val savedEntity = service.save(entity)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun `Given existing entity, when service looks for existing entity by id, then service should find using repository and return existing entity`() {
        val id = randomUUID()
        val existingEntity = TribeFactory.create(id)

        every { repository.findById(id) } returns of(existingEntity)

        val foundEntity = service.find(id)

        foundEntity shouldBe existingEntity

        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun `Given existing entity, when service deletes existing entity, then service should delete using repository`() {
        val existingEntity = TribeFactory.create()

        every { repository.findById(existingEntity.id) } returns of(existingEntity)
        every { repository.delete(existingEntity) } returns Unit
        every { squadService.deleteRelatedByTribe(existingEntity.id) } returns existingEntity.squads

        val removedEntity = service.delete(existingEntity)

        removedEntity shouldBe existingEntity

        verify(exactly = 1) { repository.delete(existingEntity) }
    }

    @Test
    fun `Given a non-existent entity UUID, when service tries to find by said UUID, then service should throw EntityNotFoundException`() {
        val id = randomUUID()

        every { repository.findById(id) } returns empty()

        assertThrows(EntityNotFoundException::class.java) { service.find(id) }
    }
}
