package app.choppa.domain.history

import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class HistoryService(
    @Autowired private val historyRepository: HistoryRepository
) {
    fun find(): List<History> = historyRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No history records exist yet.") }

    fun save(history: History): History = historyRepository
        .save(history)

    fun save(history: List<History>): List<History> = historyRepository
        .saveAll(history)

    fun delete(history: History): History = history
        .apply { historyRepository.delete(history) }

    fun delete(history: List<History>): List<History> = history
        .apply { historyRepository.deleteAll(history) }

    fun findRelatedByChapter(chapterId: UUID): List<History> = historyRepository
        .findAllByChapterIdOrderByCreateDate(chapterId)
        .orElseThrow { throw EntityNotFoundException("No history records found for chapter [$chapterId].") }

    fun findRelatedByMember(memberId: UUID): List<History> = historyRepository
        .findAllByMemberIdOrderByCreateDate(memberId)
        .orElseThrow { throw EntityNotFoundException("No history records found for member [$memberId].") }

    fun findRelatedBySquad(squadId: UUID): List<History> = historyRepository
        .findAllBySquadIdOrderByCreateDate(squadId)
        .orElseThrow { throw EntityNotFoundException("No history records found for squad [$squadId].") }

    fun findRelatedByTribe(tribeId: UUID): List<History> = historyRepository
        .findAllByTribeIdOrderByCreateDate(tribeId)
        .orElseThrow { throw EntityNotFoundException("No history records found for tribe [$tribeId].") }

    fun findRelatedByIteration(iterationId: UUID): List<History> = historyRepository
        .findAllByIterationIdOrderByCreateDate(iterationId)
        .orElseThrow { throw EntityNotFoundException("No history records found for iteration [$iterationId].") }

    fun findAllByCreateDateBefore(createDate: Instant): List<History> = historyRepository
        .findAllByCreateDateBeforeOrderByCreateDate(createDate)
        .orElseThrow { throw EntityNotFoundException("No history records found before date [$createDate].") }

    fun findAllByCreateDateAfter(createDate: Instant): List<History> = historyRepository
        .findAllByCreateDateAfterOrderByCreateDate(createDate)
        .orElseThrow { throw EntityNotFoundException("No history records found after date [$createDate].") }

    private fun <E> List<E>.orElseThrow(exception: () -> Nothing): List<E> = when {
        this.isEmpty() -> exception.invoke()
        else -> this
    }
}
