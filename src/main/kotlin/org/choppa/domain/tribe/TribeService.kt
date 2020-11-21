package org.choppa.domain.tribe

import org.choppa.exception.EmptyListException
import org.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class TribeService(
    @Autowired private val tribeRepository: TribeRepository,
) {
    fun find(id: UUID): Tribe {
        return tribeRepository.findById(id).orElseThrow {
            throw EntityNotFoundException("Tribe with id [$id] does not exist.")
        }
    }

    fun find(): List<Tribe> {
        val tribes = tribeRepository.findAll()
        return if (tribes.isEmpty()) throw EmptyListException("No tribes exist yet.") else tribes
    }

    fun find(ids: List<UUID>): List<Tribe> {
        return tribeRepository.findAllById(ids)
    }

    @Transactional
    fun save(tribe: Tribe): Tribe {
        return tribeRepository.save(tribe)
    }

    @Transactional
    fun save(tribes: List<Tribe>): List<Tribe> {
        return tribeRepository.saveAll(tribes)
    }

    @Transactional
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
