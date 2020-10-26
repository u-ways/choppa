package org.choppa.acceptance.service

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeNull
import org.choppa.model.Tribe
import org.choppa.repository.TribeRepository
import org.choppa.service.SquadService
import org.choppa.service.TribeService
import org.choppa.service.relations.IterationHistoryService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional.empty
import java.util.Optional.of
import java.util.UUID.randomUUID

private const val TRIBE_NAME = "tribeName"

internal class TribeServiceTest {
    private lateinit var repository: TribeRepository
    private lateinit var iterationHistoryService: IterationHistoryService
    private lateinit var squadService: SquadService
    private lateinit var service: TribeService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(TribeRepository::class)
        squadService = mockkClass(SquadService::class, relaxed = true)
        iterationHistoryService = mockkClass(IterationHistoryService::class, relaxed = true)

        service = TribeService(repository, squadService, iterationHistoryService)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = Tribe(name = TRIBE_NAME)

        every { repository.save(entity) } returns entity

        val savedEntity = service.save(entity)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun `Given existing entity, when service looks for existing entity by id, then service should find using repository and return existing entity`() {
        val id = randomUUID()
        val existingEntity = Tribe(id, TRIBE_NAME)

        every { repository.findById(id) } returns of(existingEntity)

        val foundEntity = service.find(id)

        foundEntity shouldBe existingEntity

        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun `Given existing entity, when service deletes existing entity, then service should delete using repository`() {
        val existingEntity = Tribe(randomUUID(), TRIBE_NAME)

        every { repository.delete(existingEntity) } returns Unit
        every { repository.findById(existingEntity.id) } returns empty()

        val removedEntity = service.delete(existingEntity)

        removedEntity shouldBe existingEntity

        val nonExistentEntity = service.find(existingEntity.id)

        nonExistentEntity.shouldBeNull()

        verify(exactly = 1) { repository.delete(existingEntity) }
    }
}
