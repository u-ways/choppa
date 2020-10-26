package org.choppa.service

import org.choppa.model.Chapter
import org.choppa.repository.ChapterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChapterService(
    @Autowired private val chapterRepository: ChapterRepository
) {
    fun find(id: UUID): Chapter? {
        return chapterRepository.findById(id).orElseGet { null }
    }

    fun find(): List<Chapter> {
        return chapterRepository.findAll()
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
