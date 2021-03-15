package app.choppa.acceptance.domain.member

import app.choppa.domain.account.AccountService
import app.choppa.domain.member.MemberRepository
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.factory.ChapterFactory
import app.choppa.support.factory.MemberFactory
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

internal class MemberServiceTest {
    private lateinit var repository: MemberRepository
    private lateinit var squadMemberHistoryService: SquadMemberHistoryService
    private lateinit var accountService: AccountService
    private lateinit var service: MemberService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(MemberRepository::class)
        squadMemberHistoryService = mockkClass(SquadMemberHistoryService::class)
        accountService = mockkClass(AccountService::class, relaxed = true)
        service = MemberService(repository, squadMemberHistoryService, accountService)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val relatedEntityChapter = ChapterFactory.create()
        val entity = MemberFactory.create(chapter = relatedEntityChapter)

        every { repository.save(entity) } returns entity
        every { repository.findById(entity.id) } returns empty()

        val savedEntity = service.save(entity)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun `Given existing entity, when service looks for existing entity by id, then service should find using repository and return existing entity`() {
        val id = randomUUID()
        val relatedEntityChapter = ChapterFactory.create()
        val existingEntity = MemberFactory.create(id, chapter = relatedEntityChapter)

        every { repository.findById(id) } returns of(existingEntity)

        val foundEntity = service.find(id)

        foundEntity shouldBe existingEntity

        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun `Given existing entity, when service deletes existing entity, then service should delete using repository`() {
        val existingEntity = MemberFactory.create()

        every { repository.delete(existingEntity) } returns Unit
        every { repository.findById(existingEntity.id) } returns of(existingEntity)
        every { repository.deleteAllSquadMemberRecordsFor(existingEntity.id) } returns Unit
        every { squadMemberHistoryService.deleteAllFor(existingEntity) } returns existingEntity

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
