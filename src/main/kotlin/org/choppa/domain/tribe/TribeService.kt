package org.choppa.domain.tribe

import org.choppa.domain.base.BaseService
import org.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TribeService(
    @Autowired private val tribeRepository: TribeRepository,
) : BaseService() {
    fun find(id: UUID): Tribe = tribeRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Tribe with id [$id] does not exist.") }

    fun find(): List<Tribe> = tribeRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No tribes exist yet.") }

    fun find(ids: List<UUID>): List<Tribe> = tribeRepository
        .findAllById(ids)
        .orElseThrow { throw EntityNotFoundException("No tribes found with given ids.") }

    fun save(tribe: Tribe): Tribe {
        return tribeRepository.save(tribe)
    }

    fun save(tribes: List<Tribe>): List<Tribe> {
        return tribeRepository.saveAll(tribes)
    }

    fun save(vararg tribes: Tribe): List<Tribe> {
        return save(tribes.toMutableList())
    }

    fun delete(tribe: Tribe): Tribe {
        tribeRepository.delete(tribe)
        return tribe
    }

    fun delete(tribes: List<Tribe>): List<Tribe> {
        tribeRepository.deleteAll(tribes)
        return tribes
    }

    fun delete(vararg tribes: Tribe): List<Tribe> {
        return delete(tribes.toMutableList())
    }
}
