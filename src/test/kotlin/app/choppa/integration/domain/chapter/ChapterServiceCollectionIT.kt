package app.choppa.integration.domain.chapter

import app.choppa.domain.account.Account.Companion.UNASSIGNED_ACCOUNT
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.factory.ChapterFactory
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.matchers.containsInAnyOrder
import app.choppa.support.testcontainers.TestDBContainer
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
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
internal class ChapterServiceCollectionIT @Autowired constructor(
    private val chapterService: ChapterService,
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun `Given a new list of chapters, when service saves said list of chapters, then service should persist the list of chapters`() {
        val newListOfChapters = ChapterFactory.create(amount = 3)
        val result = chapterService.save(newListOfChapters, UNASSIGNED_ACCOUNT)

        assertThat(result, List<Chapter>::containsInAnyOrder, newListOfChapters)
    }

    @Test
    @Transactional
    fun `Given an existing list of chapters, when service updates said list of chapters, then service should persist the changed list of chapters`() {
        val existingListOfChapters = chapterService.save(ChapterFactory.create(amount = 3), UNASSIGNED_ACCOUNT)
        val newName = "newName"

        val updatedListOfChapters = existingListOfChapters.map {
            Chapter(it.id, newName)
        }

        val result = chapterService.save(updatedListOfChapters, UNASSIGNED_ACCOUNT)

        assertThat(result, List<Chapter>::containsInAnyOrder, updatedListOfChapters)
    }

    @Test
    @Transactional
    fun `Given an existing list of chapters, when service deletes said list of chapters, then service should remove the existing list of chapters`() {
        val existingListOfChapters = chapterService.save(ChapterFactory.create(amount = 3), UNASSIGNED_ACCOUNT)
        val removedListOfChapters = chapterService.delete(existingListOfChapters, UNASSIGNED_ACCOUNT)

        assertThrows(EntityNotFoundException::class.java) { chapterService.find(removedListOfChapters.map { it.id }, UNASSIGNED_ACCOUNT) }
    }
}
