package app.choppa.acceptance.domain.history

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.history.History
import app.choppa.domain.history.HistoryId
import app.choppa.domain.history.HistoryRepository
import app.choppa.domain.history.HistoryService
import app.choppa.domain.iteration.Iteration
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant.now
import java.util.Optional.empty

internal class HistoryServiceTest {
    private lateinit var repository: HistoryRepository
    private lateinit var service: HistoryService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(HistoryRepository::class)
        service = HistoryService(repository)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = History(Iteration(), Tribe(), Squad(), Member(), Chapter())

        every { repository.save(entity) } returns entity

        val savedEntity = service.save(entity)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun `Given existing entities related by chapter, when service looks for existing entities by chapter id, then service should find using repository and return existing entities related by chapter`() {
        val relatedChapter = Chapter()
        val existingRecord = History(Iteration(), Tribe(), Squad(), Member(), relatedChapter)

        every { repository.findAllByChapterIdOrderByCreateDate(relatedChapter.id) } returns listOf(existingRecord)

        val foundRecords = service.findRelatedByChapter(relatedChapter.id)

        foundRecords shouldBeEqualTo listOf(existingRecord)

        verify(exactly = 1) { repository.findAllByChapterIdOrderByCreateDate(relatedChapter.id) }
    }

    @Test
    fun `Given existing entities related by member, when service looks for existing entities by member id, then service should find using repository and return existing entities related by member`() {
        val relatedMember = Member()
        val existingRecord = History(Iteration(), Tribe(), Squad(), relatedMember, Chapter())

        every { repository.findAllByMemberIdOrderByCreateDate(relatedMember.id) } returns listOf(existingRecord)

        val foundRecords = service.findRelatedByMember(relatedMember.id)

        foundRecords shouldBeEqualTo listOf(existingRecord)

        verify(exactly = 1) { repository.findAllByMemberIdOrderByCreateDate(relatedMember.id) }
    }

    @Test
    fun `Given existing entities related by squad, when service looks for existing entities by squad id, then service should find using repository and return existing entities related by squad`() {
        val relatedSquad = Squad()
        val existingRecord = History(Iteration(), Tribe(), relatedSquad, Member(), Chapter())

        every { repository.findAllBySquadIdOrderByCreateDate(relatedSquad.id) } returns listOf(existingRecord)

        val foundRecords = service.findRelatedBySquad(relatedSquad.id)

        foundRecords shouldBeEqualTo listOf(existingRecord)

        verify(exactly = 1) { repository.findAllBySquadIdOrderByCreateDate(relatedSquad.id) }
    }

    @Test
    fun `Given existing entities related by tribe, when service looks for existing entities by tribe id, then service should find using repository and return existing entities related by tribe`() {
        val relatedTribe = Tribe()
        val existingRecord = History(Iteration(), relatedTribe, Squad(), Member(), Chapter())

        every { repository.findAllByTribeIdOrderByCreateDate(relatedTribe.id) } returns listOf(existingRecord)

        val foundRecords = service.findRelatedByTribe(relatedTribe.id)

        foundRecords shouldBeEqualTo listOf(existingRecord)

        verify(exactly = 1) { repository.findAllByTribeIdOrderByCreateDate(relatedTribe.id) }
    }

    @Test
    fun `Given existing entities related by iteration, when service looks for existing entities by iteration id, then service should find using repository and return existing entities related by iteration`() {
        val relatedIteration = Iteration()
        val existingRecord = History(relatedIteration, Tribe(), Squad(), Member(), Chapter())

        every { repository.findAllByIterationIdOrderByCreateDate(relatedIteration.id) } returns listOf(existingRecord)

        val foundRecords = service.findRelatedByIteration(relatedIteration.id)

        foundRecords shouldBeEqualTo listOf(existingRecord)

        verify(exactly = 1) { repository.findAllByIterationIdOrderByCreateDate(relatedIteration.id) }
    }

    @Test
    fun `Given existing entities related by createDate, when service looks for existing entities by createDate id, then service should find using repository and return existing entities related by createDate`() {
        val createDate = now()
        val existingRecord = History(Iteration(), Tribe(), Squad(), Member(), Chapter(), createDate)

        every { repository.findAllByCreateDateBeforeOrderByCreateDate(createDate) } returns listOf(existingRecord)

        val foundRecords = service.findAllByCreateDateBefore(createDate)

        foundRecords shouldBeEqualTo listOf(existingRecord)

        verify(exactly = 1) { repository.findAllByCreateDateBeforeOrderByCreateDate(createDate) }
    }

    @Test
    fun `Given existing entity, when service deletes existing entity, then service should delete using repository`() {
        val existingEntity = History(Iteration(), Tribe(), Squad(), Member(), Chapter())
        val id = HistoryId(
            existingEntity.iteration!!.id,
            existingEntity.tribe!!.id,
            existingEntity.squad!!.id,
            existingEntity.member!!.id,
            existingEntity.chapter!!.id
        )

        every { repository.delete(existingEntity) } returns Unit
        every { repository.findById(id) } returns empty()

        val removedEntity = service.delete(existingEntity)

        removedEntity shouldBe existingEntity

        verify(exactly = 1) { repository.delete(existingEntity) }
    }
}
