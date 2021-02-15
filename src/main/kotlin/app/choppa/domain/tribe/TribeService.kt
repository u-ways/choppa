package app.choppa.domain.tribe

import app.choppa.domain.base.BaseService
import app.choppa.domain.squad.SquadService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TribeService(
    @Autowired private val tribeRepository: TribeRepository,
    @Autowired private val squadService: SquadService,
) : BaseService<Tribe> {
    override fun find(): List<Tribe> = tribeRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No tribes exist yet.") }

    override fun find(ids: List<UUID>): List<Tribe> = tribeRepository
        .findAllById(ids)
        .orElseThrow { throw EntityNotFoundException("No tribes found with given ids.") }

    override fun find(id: UUID): Tribe = tribeRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Tribe with id [$id] does not exist.") }

    override fun save(entity: Tribe): Tribe = tribeRepository
        .save(entity)

    override fun save(entities: List<Tribe>): List<Tribe> = tribeRepository
        .saveAll(entities)

    @Transactional
    override fun delete(entity: Tribe): Tribe = entity.apply {
        squadService.deleteRelatedByTribe(entity.id)
        tribeRepository.delete(entity)
    }

    @Transactional
    override fun delete(entities: List<Tribe>): List<Tribe> = entities.map(::delete)
}
