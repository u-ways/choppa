package choppaorg.choppa.service.relations

import choppaorg.choppa.model.relations.IterationHistory
import choppaorg.choppa.repository.relations.IterationHistoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class IterationHistoryService(
        @Autowired private val iterationHistoryRepository: IterationHistoryRepository
) {
    fun find(id: UUID): IterationHistory? {
        return iterationHistoryRepository.findById(id).get()
    }

    fun find(): List<IterationHistory> {
        return iterationHistoryRepository.findAll()
    }

    fun find(ids: List<UUID>): List<IterationHistory> {
        return iterationHistoryRepository.findAllById(ids)
    }

    fun save(iterationHistory: IterationHistory): IterationHistory {
        return iterationHistoryRepository.save(iterationHistory)
    }

    fun save(iterationHistory: MutableList<IterationHistory>): List<IterationHistory> {
        return iterationHistoryRepository.saveAll(iterationHistory)
    }

    fun save(vararg iterationHistory: IterationHistory): List<IterationHistory> {
        return save(iterationHistory.toMutableList())
    }

    fun delete(iterationHistory: IterationHistory): IterationHistory {
        iterationHistoryRepository.delete(iterationHistory)
        return iterationHistory
    }

    fun delete(iterationHistory: List<IterationHistory>): List<IterationHistory> {
        iterationHistoryRepository.deleteAll(iterationHistory)
        return iterationHistory
    }

    fun delete(vararg iterationHistory: IterationHistory): List<IterationHistory> {
        return delete(iterationHistory.toMutableList())
    }
}
