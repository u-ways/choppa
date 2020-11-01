package org.choppa.service.relations

import org.choppa.model.relations.IterationHistory
import org.choppa.model.relations.IterationHistoryId
import org.choppa.repository.relations.IterationHistoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class IterationHistoryService(
    @Autowired private val iterationHistoryRepository: IterationHistoryRepository
) {
    fun find(id: IterationHistoryId): IterationHistory? {
        return iterationHistoryRepository.findById(id).orElseGet { null }
    }

    fun find(): List<IterationHistory> {
        return iterationHistoryRepository.findAll()
    }

    fun find(ids: List<IterationHistoryId>): List<IterationHistory> {
        return iterationHistoryRepository.findAllById(ids)
    }

    fun save(iterationHistory: IterationHistory): IterationHistory {
        return iterationHistoryRepository.save(iterationHistory)
    }

    fun save(iterationHistory: List<IterationHistory>): List<IterationHistory> {
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
