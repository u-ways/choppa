package app.choppa.domain.tribe

import app.choppa.domain.base.BaseService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TribeService(
    @Autowired private val tribeRepository: TribeRepository,
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

    override fun delete(entity: Tribe): Tribe = entity
        .apply { tribeRepository.delete(entity) }

    override fun delete(entities: List<Tribe>): List<Tribe> = entities
        .apply { tribeRepository.deleteAll(entities) }
}
