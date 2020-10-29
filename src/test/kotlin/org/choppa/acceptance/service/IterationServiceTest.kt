package org.choppa.acceptance.service

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeNull
import org.choppa.model.Iteration
import org.choppa.repository.IterationRepository
import org.choppa.service.IterationService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional.empty
import java.util.Optional.of
import java.util.UUID.randomUUID

private const val ITERATION_NUMBER = 100
private const val ITERATION_TIMEBOX = 10

internal class IterationServiceTest {
    private lateinit var repository: IterationRepository
    private lateinit var service: IterationService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(IterationRepository::class)

        service = IterationService(repository)
    }

    @Test
    fun givenNewEntity_WhenServiceSavesNewEntity_ThenServiceShouldSaveInRepositoryAndReturnTheSameEntity() {
        val entity = Iteration(number = ITERATION_NUMBER, timebox = ITERATION_TIMEBOX)

        every { repository.save(entity) } returns entity

        val savedEntity = service.save(entity)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun givenExistingEntity_WhenServiceLooksForExistingEntityById_ThenServiceShouldFindUsingRepositoryAndReturnExistingEntity() {
        val id = randomUUID()
        val existingEntity = Iteration(id, ITERATION_NUMBER, ITERATION_TIMEBOX)

        every { repository.findById(id) } returns of(existingEntity)

        val foundEntity = service.find(id)

        foundEntity shouldBe existingEntity

        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun givenExistingEntity_WhenServiceDeletesExistingEntity_ThenServiceShouldDeleteUsingRepository() {
        val existingEntity = Iteration(randomUUID(), ITERATION_NUMBER, ITERATION_TIMEBOX)

        every { repository.delete(existingEntity) } returns Unit
        every { repository.findById(existingEntity.id) } returns empty()

        val removedEntity = service.delete(existingEntity)

        removedEntity shouldBe existingEntity

        val nonExistentEntity = service.find(existingEntity.id)

        nonExistentEntity.shouldBeNull()

        verify(exactly = 1) { repository.delete(existingEntity) }
    }
}
