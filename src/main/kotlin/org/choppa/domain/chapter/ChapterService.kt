package org.choppa.domain.chapter

import org.choppa.domain.base.BaseService
import org.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChapterService(
    @Autowired private val chapterRepository: ChapterRepository
) : BaseService() {
    fun find(id: UUID): Chapter = chapterRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Chapter with id [$id] does not exist.") }

    fun find(): List<Chapter> = chapterRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No chapters exist yet.") }

    fun findRelatedBySquad(squadId: UUID): List<Chapter> = chapterRepository
        .findAllBySquadId(squadId)
        .orElseThrow { throw EntityNotFoundException("No chapters in squad [$squadId] exist yet.") }

    fun findRelatedByTribe(tribeId: UUID): List<Chapter> = chapterRepository
        .findAllByTribeId(tribeId)
        .orElseThrow { throw EntityNotFoundException("No chapters in tribe [$tribeId] exist yet.") }

    fun find(ids: List<UUID>): List<Chapter> = chapterRepository
        .findAllById(ids)
        .orElseThrow { throw EntityNotFoundException("No chapters found with given ids.") }

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
