package org.choppa.integration.service

import org.amshove.kluent.shouldBe
import org.choppa.helpers.exception.EntityNotFoundException
import org.choppa.model.chapter.Chapter
import org.choppa.repository.ChapterRepository
import org.choppa.service.ChapterService
import org.choppa.support.flyway.FlywayMigrationConfig
import org.choppa.support.testcontainers.TestDBContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.transaction.Transactional

private const val CHAPTER_NAME = "chapterName"

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class ChapterServiceIT @Autowired constructor(
    private val chapterRepository: ChapterRepository,
    private val chapterService: ChapterService
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    fun `Given new entity, when service saves new entity, then service should return same entity with generated id`() {
        val entity = Chapter(name = CHAPTER_NAME)
        val result = chapterService.save(entity)

        result.id shouldBe entity.id
        result.name shouldBe entity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entity`() {
        val existingEntity = chapterService.save(Chapter(name = CHAPTER_NAME))
        val result = chapterService.find(existingEntity.id)

        result.id shouldBe existingEntity.id
        result.name shouldBe existingEntity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = chapterService.save(Chapter(name = CHAPTER_NAME))
        val removedEntity = chapterService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { chapterService.find(removedEntity.id) }
    }

    @AfterEach
    internal fun tearDown() {
        chapterRepository.deleteAll()
        chapterRepository.deleteAll()
    }
}
