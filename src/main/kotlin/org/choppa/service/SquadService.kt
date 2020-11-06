package org.choppa.service

import org.choppa.model.squad.Squad
import org.choppa.repository.SquadRepository
import org.choppa.service.relations.IterationHistoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class SquadService(
    @Autowired private val squadRepository: SquadRepository,
    @Autowired private val iterationHistoryService: IterationHistoryService
) {
    fun find(id: UUID): Squad? {
        return squadRepository.findById(id).orElseGet { null }
    }

    fun find(): List<Squad> {
        return squadRepository.findAll()
    }

    fun find(ids: List<UUID>): List<Squad> {
        return squadRepository.findAllById(ids)
    }

    @Transactional
    fun save(squad: Squad): Squad {
        iterationHistoryService.save(squad.iterations)
        return squadRepository.save(squad)
    }

    @Transactional
    fun save(squads: List<Squad>): List<Squad> {
        squads.forEach { iterationHistoryService.save(it.iterations) }
        return squadRepository.saveAll(squads)
    }

    @Transactional
    fun save(vararg squads: Squad): List<Squad> {
        return save(squads.toMutableList())
    }

    fun delete(squad: Squad): Squad {
        squadRepository.delete(squad)
        return squad
    }

    fun delete(squads: List<Squad>): List<Squad> {
        squadRepository.deleteAll(squads)
        return squads
    }

    fun delete(vararg squads: Squad): List<Squad> {
        return delete(squads.toMutableList())
    }
}
