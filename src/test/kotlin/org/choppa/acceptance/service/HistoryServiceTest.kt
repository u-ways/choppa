package org.choppa.acceptance.service

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeNull
import org.choppa.model.chapter.Chapter
import org.choppa.model.history.History
import org.choppa.model.history.HistoryId
import org.choppa.model.iteration.Iteration
import org.choppa.model.member.Member
import org.choppa.model.squad.Squad
import org.choppa.model.tribe.Tribe
import org.choppa.repository.HistoryRepository
import org.choppa.service.HistoryService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional.empty
import java.util.Optional.of

internal class HistoryServiceTest {
    private lateinit var chapter: Chapter
    private lateinit var member: Member
    private lateinit var squad: Squad
    private lateinit var tribe: Tribe
    private lateinit var iteration: Iteration

    private lateinit var repository: HistoryRepository
    private lateinit var service: HistoryService

    @BeforeEach
    internal fun setUp() {
        chapter = Chapter()
        member = Member()
        squad = Squad()
        tribe = Tribe()
        iteration = Iteration()

        repository = mockkClass(HistoryRepository::class)
        service = HistoryService(repository)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = History(iteration, tribe, squad, member)

        every { repository.save(entity) } returns entity

        val savedEntity = service.save(entity)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun `Given existing entity, when service looks for existing entity by id, then service should find using repository and return existing entity`() {
        val id = HistoryId(iteration.id, tribe.id, squad.id, member.id)
        val existingEntity = History(iteration, tribe, squad, member)

        every { repository.findById(id) } returns of(existingEntity)

        val foundEntity = service.find(id)

        foundEntity shouldBe existingEntity

        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun `Given existing entity, when service deletes existing entity, then service should delete using repository`() {
        val id = HistoryId(iteration.id, tribe.id, squad.id, member.id)
        val existingEntity = History(iteration, tribe, squad, member)

        every { repository.delete(existingEntity) } returns Unit
        every { repository.findById(id) } returns empty()

        val removedEntity = service.delete(existingEntity)

        removedEntity shouldBe existingEntity

        val nonExistentEntity = service.find(id)

        nonExistentEntity.shouldBeNull()

        verify(exactly = 1) { repository.delete(existingEntity) }
    }
}
