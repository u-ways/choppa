package app.choppa.integration.domain.chapter

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterService
import app.choppa.domain.member.MemberService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.ChapterFactory
import app.choppa.support.factory.MemberFactory
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

internal class ChapterServiceIT @Autowired constructor(
    private val chapterService: ChapterService,
    private val memberService: MemberService,
) : BaseServiceIT() {
    private lateinit var entity: Chapter

    @BeforeEach
    internal fun setUp() {
        entity = chapterService.save(ChapterFactory.create())
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should return same entity with generated id`() {
        val result = chapterService.save(entity)

        result.id shouldBe entity.id
        result.name shouldBe entity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entity`() {
        val existingEntity = entity
        val result = chapterService.find(existingEntity.id)

        result.id shouldBe existingEntity.id
        result.name shouldBe existingEntity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = chapterService.save(ChapterFactory.create())
        val removedEntity = chapterService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { chapterService.find(removedEntity.id) }
    }

    @Test
    @Transactional
    fun `Given existing entity in db with related records, when service deletes entity, then service should removes entity and related records from db`() {
        val existingEntity = chapterService.save(ChapterFactory.create())
        val relatedMember = memberService.save(MemberFactory.create(chapter = existingEntity))

        memberService.findRelatedByChapter(existingEntity.id).first() shouldBeEqualTo relatedMember

        val removedEntity = chapterService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { memberService.findRelatedByChapter(removedEntity.id) }
    }

    @AfterEach
    internal fun tearDown() {
        chapterService.delete(entity)
    }
}
