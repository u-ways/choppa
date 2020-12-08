package app.choppa.domain.history

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface HistoryRepository : JpaRepository<History, HistoryId> {
    fun findAllByChapterIdOrderByCreateDate(chapterId: UUID): List<History>
    fun findAllByMemberIdOrderByCreateDate(memberId: UUID): List<History>
    fun findAllBySquadIdOrderByCreateDate(squadId: UUID): List<History>
    fun findAllByTribeIdOrderByCreateDate(tribeId: UUID): List<History>
    fun findAllByIterationIdOrderByCreateDate(iterationId: UUID): List<History>
    fun findAllByCreateDateBeforeOrderByCreateDate(createDate: Instant): List<History>
    fun findAllByCreateDateAfterOrderByCreateDate(createDate: Instant): List<History>
}
