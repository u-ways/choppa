package org.choppa.integration.service

import org.amshove.kluent.shouldBe
import org.choppa.exception.EntityNotFoundException
import org.choppa.model.chapter.Chapter
import org.choppa.model.member.Member
import org.choppa.service.ChapterService
import org.choppa.service.MemberService
import org.choppa.support.flyway.FlywayMigrationConfig
import org.choppa.support.testcontainers.TestDBContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.transaction.Transactional

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class MemberServiceIT @Autowired constructor(
    private val chapterService: ChapterService,
    private val memberService: MemberService
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    private lateinit var entity: Member
    private lateinit var relatedEntityChapter: Chapter

    @BeforeEach
    internal fun setUp() {
        relatedEntityChapter = chapterService.save(Chapter())
        entity = memberService.save(Member(chapter = relatedEntityChapter))
    }

    @Test
    @Transactional
    fun `Given new entity, when service saves new entity, then service should return same entity with generated id`() {
        val result = memberService.save(entity)

        result.id shouldBe entity.id
        result.name shouldBe entity.name
        result.chapter.id shouldBe entity.chapter.id
        result.chapter.name shouldBe entity.chapter.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entity`() {
        val existingEntity = entity
        val result = memberService.find(existingEntity.id)

        result.id shouldBe existingEntity.id
        result.name shouldBe existingEntity.name
        result.chapter.id shouldBe existingEntity.chapter.id
        result.chapter.name shouldBe existingEntity.chapter.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = entity
        val removedEntity = memberService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { chapterService.find(removedEntity.id) }
    }

    @AfterEach
    internal fun tearDown() {
        memberService.delete(entity)
        chapterService.delete(relatedEntityChapter)
    }
}
