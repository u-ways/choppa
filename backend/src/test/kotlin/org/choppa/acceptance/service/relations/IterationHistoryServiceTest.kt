package org.choppa.acceptance.service.relations

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeNull
import org.choppa.model.Chapter
import org.choppa.model.Iteration
import org.choppa.model.Member
import org.choppa.model.Squad
import org.choppa.model.Tribe
import org.choppa.model.relations.IterationHistory
import org.choppa.model.relations.IterationHistoryId
import org.choppa.repository.relations.IterationHistoryRepository
import org.choppa.service.relations.IterationHistoryService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional.empty
import java.util.Optional.of
import java.util.UUID.randomUUID

private val CHAPTER = Chapter(id = randomUUID(), name = "chapterName")
private val MEMBER = Member(id = randomUUID(), name = "memberName", chapter = CHAPTER)
private val SQUAD = Squad(id = randomUUID(), name = "squadName")
private val TRIBE = Tribe(id = randomUUID(), name = "tribeName")
private val ITERATION = Iteration(id = randomUUID(), number = 100, timebox = 10)

internal class IterationHistoryServiceTest {
    private lateinit var repository: IterationHistoryRepository
    private lateinit var service: IterationHistoryService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(IterationHistoryRepository::class)
        service = IterationHistoryService(repository)
    }

    @Test
    fun givenNewEntity_WhenServiceSavesNewEntity_ThenServiceShouldSaveInRepositoryAndReturnTheSameEntity() {
        val entity = IterationHistory(ITERATION, TRIBE, SQUAD, MEMBER)

        every { repository.save(entity) } returns entity

        val savedEntity = service.save(entity)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun givenExistingEntity_WhenServiceLooksForExistingEntityById_ThenServiceShouldFindUsingRepositoryAndReturnExistingEntity() {
        val id = IterationHistoryId(ITERATION.id, TRIBE.id, SQUAD.id, MEMBER.id)
        val existingEntity = IterationHistory(ITERATION, TRIBE, SQUAD, MEMBER)

        every { repository.findById(id) } returns of(existingEntity)

        val foundEntity = service.find(id)

        foundEntity shouldBe existingEntity

        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun givenExistingEntity_WhenServiceDeletesExistingEntity_ThenServiceShouldDeleteUsingRepository() {
        val id = IterationHistoryId(ITERATION.id, TRIBE.id, SQUAD.id, MEMBER.id)
        val existingEntity = IterationHistory(ITERATION, TRIBE, SQUAD, MEMBER)

        every { repository.delete(existingEntity) } returns Unit
        every { repository.findById(id) } returns empty()

        val removedEntity = service.delete(existingEntity)

        removedEntity shouldBe existingEntity

        val nonExistentEntity = service.find(id)

        nonExistentEntity.shouldBeNull()

        verify(exactly = 1) { repository.delete(existingEntity) }
    }
}
