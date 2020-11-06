package org.choppa.acceptance.service

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeNull
import org.choppa.model.chapter.Chapter
import org.choppa.model.member.Member
import org.choppa.repository.MemberRepository
import org.choppa.service.ChapterService
import org.choppa.service.HistoryService
import org.choppa.service.MemberService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional.empty
import java.util.Optional.of
import java.util.UUID.randomUUID

private const val MEMBER_NAME = "memberName"
private val CHAPTER = Chapter(id = randomUUID(), name = "chapterName")

internal class MemberServiceTest {
    private lateinit var repository: MemberRepository
    private lateinit var chapterService: ChapterService
    private lateinit var historyService: HistoryService
    private lateinit var service: MemberService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(MemberRepository::class)
        chapterService = mockkClass(ChapterService::class, relaxed = true)
        historyService = mockkClass(HistoryService::class, relaxed = true)

        service = MemberService(repository, chapterService, historyService)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = Member(name = MEMBER_NAME, chapter = CHAPTER)

        every { repository.save(entity) } returns entity

        val savedEntity = service.save(entity)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun `Given existing entity, when service looks for existing entity by id, then service should find using repository and return existing entity`() {
        val id = randomUUID()
        val existingEntity = Member(id, MEMBER_NAME, CHAPTER)

        every { repository.findById(id) } returns of(existingEntity)

        val foundEntity = service.find(id)

        foundEntity shouldBe existingEntity

        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun `Given existing entity, when service deletes existing entity, then service should delete using repository`() {
        val existingEntity = Member(randomUUID(), MEMBER_NAME, CHAPTER)

        every { repository.delete(existingEntity) } returns Unit
        every { repository.findById(existingEntity.id) } returns empty()

        val removedEntity = service.delete(existingEntity)

        removedEntity shouldBe existingEntity

        val nonExistentEntity = service.find(existingEntity.id)

        nonExistentEntity.shouldBeNull()

        verify(exactly = 1) { repository.delete(existingEntity) }
    }
}
