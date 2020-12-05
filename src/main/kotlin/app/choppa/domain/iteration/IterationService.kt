package app.choppa.domain.iteration

import app.choppa.domain.base.BaseService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class IterationService(
    @Autowired private val iterationRepository: IterationRepository
) : BaseService<Iteration> {
    override fun find(): List<Iteration> = iterationRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No iterations exist yet.") }

    override fun find(id: UUID): Iteration = iterationRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Iteration with id [$id] does not exist.") }

    override fun find(ids: List<UUID>): List<Iteration> = iterationRepository
        .findAllById(ids)
        .orElseThrow { throw EntityNotFoundException("No iterations found with given ids.") }

    override fun save(entity: Iteration): Iteration = iterationRepository
        .save(entity)

    override fun save(entities: List<Iteration>): List<Iteration> = iterationRepository
        .saveAll(entities)

    override fun delete(entity: Iteration): Iteration = entity
        .apply { iterationRepository.delete(entity) }

    override fun delete(entities: List<Iteration>): List<Iteration> = entities
        .apply { iterationRepository.deleteAll(entities) }

    fun findRelatedByMember(memberId: UUID): List<Iteration> = iterationRepository
        .findAllByMemberId(memberId)
        .orElseThrow { throw EntityNotFoundException("No iterations found for member [$memberId].") }

    fun findRelatedBySquad(squadId: UUID): List<Iteration> = iterationRepository
        .findAllBySquadId(squadId)
        .orElseThrow { throw EntityNotFoundException("No iterations found for squad [$squadId].") }

    fun findRelatedByTribe(tribeId: UUID): List<Iteration> = iterationRepository
        .findAllByTribeId(tribeId)
        .orElseThrow { throw EntityNotFoundException("No iterations found for tribe [$tribeId].") }
}
