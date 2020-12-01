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
    override fun find(): List<Chapter> = chapterRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No chapters exist yet.") }

    override fun find(id: UUID): Chapter = chapterRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Chapter with id [$id] does not exist.") }

    override fun find(ids: List<UUID>): List<Chapter> = chapterRepository
        .findAllById(ids)
        .orElseThrow { throw EntityNotFoundException("No chapters found with given ids.") }

    override fun save(entity: Chapter): Chapter = chapterRepository
        .save(entity)

    override fun save(entities: List<Chapter>): List<Chapter> = chapterRepository
        .saveAll(entities)

    override fun delete(entities: List<Chapter>): List<Chapter> = entities
        .apply { chapterRepository.deleteAll(entities) }

    override fun delete(entity: Chapter): Chapter = entity
        .apply { chapterRepository.delete(entity) }

    fun findRelatedBySquad(squadId: UUID): List<Chapter> = chapterRepository
        .findAllBySquadId(squadId)
        .orElseThrow { throw EntityNotFoundException("No chapters in squad [$squadId] exist yet.") }

    fun findRelatedByTribe(tribeId: UUID): List<Chapter> = chapterRepository
        .findAllByTribeId(tribeId)
        .orElseThrow { throw EntityNotFoundException("No chapters in tribe [$tribeId] exist yet.") }
}
