package choppaorg.choppa.service

import choppaorg.choppa.model.Tribe
import choppaorg.choppa.repository.TribeRepository
import choppaorg.choppa.service.relations.IterationHistoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TribeService(
        @Autowired private val tribeRepository: TribeRepository,
        @Autowired private val squadService: SquadService,
        @Autowired private val iterationHistoryService: IterationHistoryService
) {
    fun find(id: UUID): Tribe? {
        return tribeRepository.findById(id).get()
    }

    fun find(): List<Tribe> {
        return tribeRepository.findAll()
    }

    fun find(ids: List<UUID>): List<Tribe> {
        return tribeRepository.findAllById(ids)
    }

    @Transactional
    fun save(tribe: Tribe): Tribe {
        squadService.save(tribe.squads)
        iterationHistoryService.save(tribe.iterations)
        return tribeRepository.save(tribe)
    }

    @Transactional
    fun save(tribes: List<Tribe>): List<Tribe> {
        tribes.forEach { squadService.save(it.squads) }
        tribes.forEach { iterationHistoryService.save(it.iterations) }
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
