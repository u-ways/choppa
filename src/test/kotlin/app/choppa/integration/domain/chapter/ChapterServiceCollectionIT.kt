package app.choppa.integration.domain.chapter

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.ChapterFactory
import app.choppa.support.matchers.containsInAnyOrder
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

internal class ChapterServiceCollectionIT @Autowired constructor(
    private val chapterService: ChapterService,
) : BaseServiceIT() {
    @Test
    @Transactional
    fun `Given a new list of chapters, when service saves said list of chapters, then service should persist the list of chapters`() {
        val newListOfChapters = ChapterFactory.create(amount = 3)
        val result = chapterService.save(newListOfChapters)

        assertThat(result, List<Chapter>::containsInAnyOrder, newListOfChapters)
    }

    @Test
    @Transactional
    fun `Given an existing list of chapters, when service updates said list of chapters, then service should persist the changed list of chapters`() {
        val existingListOfChapters = chapterService.save(ChapterFactory.create(amount = 3))
        val newName = "newName"

        val updatedListOfChapters = existingListOfChapters.map {
            ChapterFactory.create(it.id, newName)
        }

        val result = chapterService.save(updatedListOfChapters)

        assertThat(result, List<Chapter>::containsInAnyOrder, updatedListOfChapters)
    }

    @Test
    @Transactional
    fun `Given an existing list of chapters, when service deletes said list of chapters, then service should remove the existing list of chapters`() {
        val existingListOfChapters = chapterService.save(ChapterFactory.create(amount = 3))
        val removedListOfChapters = chapterService.delete(existingListOfChapters)

        assertThrows(EntityNotFoundException::class.java) { chapterService.find(removedListOfChapters.map { it.id }) }
    }
}
