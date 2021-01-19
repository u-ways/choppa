package app.choppa.domain.squad.history

import app.choppa.domain.squad.Squad
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest.of
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.domain.Sort.by
import org.springframework.stereotype.Service

@Service
class SquadMemberHistoryService(
    @Autowired private val squadHistoryRepository: SquadMemberHistoryRepository,
) {
    fun find(): List<SquadMemberHistory> = squadHistoryRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No Squad Member History records exist yet.") }

    fun save(entity: SquadMemberHistory): SquadMemberHistory = squadHistoryRepository
        .save(entity)

    fun save(entities: List<SquadMemberHistory>): List<SquadMemberHistory> = squadHistoryRepository
        .saveAll(entities)

    fun delete(entity: SquadMemberHistory): SquadMemberHistory = entity
        .apply { squadHistoryRepository.delete(entity) }

    fun delete(entities: List<SquadMemberHistory>): List<SquadMemberHistory> = entities
        .apply { squadHistoryRepository.deleteAll(entities) }

    fun findAllSquadMemberRevisions(squad: Squad) = squadHistoryRepository
        .findAllBySquad(squad, by(DESC, SquadMemberHistory::revisionNumber.name))
        .orElseThrow { throw EntityNotFoundException("No Squad Member History records exist for Squad [${squad.id}] yet.") }

    fun findLastNSquadMemberRevisions(squad: Squad, revisionAmount: Int) = squadHistoryRepository
        .findAllBySquad(squad, of(0, revisionAmount, by(DESC, SquadMemberHistory::revisionNumber.name)))
        .orElseThrow { throw EntityNotFoundException("No Squad Member History records exist for Squad [${squad.id}] yet.") }

    fun findSquadMemberRevisionsAfter(squad: Squad, revisionNumber: Int) = squadHistoryRepository
        .findAllBySquadAndRevisionNumberAfter(squad, revisionNumber)
        .orElseThrow { throw EntityNotFoundException("No Squad Member History records exist after revision number [$revisionNumber] for Squad [${squad.id}].") }

    private fun <E> List<E>.orElseThrow(exception: () -> Nothing): List<E> = when {
        this.isEmpty() -> exception.invoke()
        else -> this
    }
}
