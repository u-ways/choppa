package org.choppa.domain.chapter

import org.choppa.exception.EmptyListException
import org.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChapterService(
    @Autowired private val chapterRepository: ChapterRepository
) {
    fun find(id: UUID): Chapter {
        return chapterRepository.findById(id).orElseThrow {
            throw EntityNotFoundException("Chapter with id [$id] does not exist.")
        }
    }

    fun find(): List<Chapter> {
        val chapters = chapterRepository.findAll()
        return if (chapters.isEmpty()) throw EmptyListException("No chapters exist yet.") else chapters
    }

    fun find(ids: List<UUID>): List<Chapter> {
        return chapterRepository.findAllById(ids)
    }

    fun save(chapter: Chapter): Chapter {
        return chapterRepository.save(chapter)
    }

    fun save(chapters: List<Chapter>): List<Chapter> {
        return chapterRepository.saveAll(chapters)
    }

    fun save(vararg chapters: Chapter): List<Chapter> {
        return save(chapters.toMutableList())
    }

    fun delete(chapter: Chapter): Chapter {
        chapterRepository.delete(chapter)
        return chapter
    }

    fun delete(chapters: List<Chapter>): List<Chapter> {
        chapterRepository.deleteAll(chapters)
        return chapters
    }

    fun delete(vararg chapters: Chapter): List<Chapter> {
        return delete(chapters.toMutableList())
    }
}
