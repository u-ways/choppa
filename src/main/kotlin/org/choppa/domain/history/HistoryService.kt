package org.choppa.domain.history

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HistoryService(
    @Autowired private val historyRepository: HistoryRepository
) {
    fun find(id: HistoryId): History? {
        return historyRepository.findById(id).orElseGet { null }
    }

    fun find(): List<History> {
        return historyRepository.findAll()
    }

    fun find(ids: List<HistoryId>): List<History> {
        return historyRepository.findAllById(ids)
    }

    fun save(history: History): History {
        return historyRepository.save(history)
    }

    fun save(history: List<History>): List<History> {
        return historyRepository.saveAll(history)
    }

    fun save(vararg history: History): List<History> {
        return save(history.toMutableList())
    }

    fun delete(history: History): History {
        historyRepository.delete(history)
        return history
    }

    fun delete(history: List<History>): List<History> {
        historyRepository.deleteAll(history)
        return history
    }

    fun delete(vararg history: History): List<History> {
        return delete(history.toMutableList())
    }
}
