package app.choppa.acceptance.domain.chapter

import app.choppa.domain.account.Account.Companion.UNASSIGNED_ACCOUNT
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterRepository
import app.choppa.domain.chapter.ChapterService
import app.choppa.domain.member.MemberService
import app.choppa.exception.EntityNotFoundException
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

internal class ChapterServiceTest {
    private lateinit var repository: ChapterRepository
    private lateinit var memberService: MemberService
    private lateinit var service: ChapterService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(ChapterRepository::class)
        memberService = mockkClass(MemberService::class)
        service = ChapterService(repository, memberService)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = Chapter()

        every { repository.findById(entity.id) } returns empty()
        every { repository.save(entity) } returns entity

        val savedEntity = service.save(entity, UNASSIGNED_ACCOUNT)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun `Given existing entity, when service looks for existing entity by id, then service should find using repository and return existing entity`() {
        val id = randomUUID()
        val existingEntity = Chapter(id)

        every { repository.findById(id) } returns of(existingEntity)

        val foundEntity = service.find(id, UNASSIGNED_ACCOUNT)

        foundEntity shouldBe existingEntity

        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun `Given existing entity, when service deletes existing entity, then service should delete using repository`() {
        val existingEntity = Chapter()

        every { repository.findById(existingEntity.id) } returns of(existingEntity)
        every { repository.delete(existingEntity) } returns Unit
        every { memberService.unAssignMembersWithChapter(existingEntity, UNASSIGNED_ACCOUNT) } returns Unit

        val removedEntity = service.delete(existingEntity, UNASSIGNED_ACCOUNT)

        removedEntity shouldBe existingEntity

        verify(exactly = 1) { repository.delete(existingEntity) }
    }

    @Test
    fun `Given a non-existent entity UUID, when service tries to find by said UUID, then service should throw EntityNotFoundException`() {
        val id = randomUUID()

        every { repository.findById(id) } returns empty()

        assertThrows(EntityNotFoundException::class.java) { service.find(id, UNASSIGNED_ACCOUNT) }
    }
}
