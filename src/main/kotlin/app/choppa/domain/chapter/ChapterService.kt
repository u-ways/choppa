package app.choppa.domain.chapter

import app.choppa.domain.base.BaseService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChapterService(
    @Autowired private val chapterRepository: ChapterRepository
) : BaseService<Chapter> {
    override fun find(id: UUID): Chapter = chapterRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Chapter with id [$id] does not exist.") }

    override fun save(entity: Chapter): Chapter = chapterRepository
        .save(entity)

    override fun delete(entity: Chapter): Chapter = entity
        .apply { chapterRepository.delete(entity) }

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

    fun save(chapters: List<Chapter>): List<Chapter> = chapterRepository
        .saveAll(chapters)

    fun save(vararg chapters: Chapter): List<Chapter> = this
        .save(chapters.toMutableList())

    fun delete(chapters: List<Chapter>): List<Chapter> = chapters
        .apply { chapterRepository.deleteAll(chapters) }

    fun delete(vararg chapters: Chapter): List<Chapter> = this
        .delete(chapters.toMutableList())
}
