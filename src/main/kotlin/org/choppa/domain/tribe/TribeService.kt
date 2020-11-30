package org.choppa.domain.tribe

import org.choppa.domain.base.BaseService
import org.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TribeService(
    @Autowired private val tribeRepository: TribeRepository,
) : BaseService<Tribe> {
    override fun find(id: UUID): Tribe = tribeRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Tribe with id [$id] does not exist.") }

    override fun save(entity: Tribe): Tribe = tribeRepository
        .save(entity)

    override fun delete(entity: Tribe): Tribe = entity
        .apply { tribeRepository.delete(entity) }

    fun find(): List<Tribe> = tribeRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No tribes exist yet.") }

    fun find(ids: List<UUID>): List<Tribe> = tribeRepository
        .findAllById(ids)
        .orElseThrow { throw EntityNotFoundException("No tribes found with given ids.") }

    fun save(tribes: List<Tribe>): List<Tribe> = tribeRepository
        .saveAll(tribes)

    fun save(vararg tribes: Tribe): List<Tribe> = this
        .save(tribes.toMutableList())

    fun delete(tribes: List<Tribe>): List<Tribe> = tribes
        .apply { tribeRepository.deleteAll(tribes) }

    fun delete(vararg tribes: Tribe): List<Tribe> = this
        .delete(tribes.toMutableList())
}
