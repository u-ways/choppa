package app.choppa.domain.squad.history

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.domain.Sort.by
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SquadMemberHistoryRepository : JpaRepository<SquadMemberHistory, SquadMemberHistoryId> {

    @Query(
        value =
            """
            SELECT DISTINCT smh.squad_id, smh.member_id, smh.revision_number, smh.revision_type, smh.create_date
            FROM squad_member_history AS smh
            INNER JOIN squad ON smh.squad_id = squad.squad_id
            WHERE squad.account_id = cast(:accountId AS UUID)
            order by smh.create_date desc
        """,
        nativeQuery = true
    )
    fun findAllByAccountIdOrderByCreateDateDesc(
        @Param("accountId") accountId: UUID,
        pageable: Pageable
    ): Page<SquadMemberHistory>

    fun findAllBySquad(
        squad: Squad,
        pageable: Pageable,
    ): List<SquadMemberHistory>

    fun findAllBySquad(
        squad: Squad,
        sort: Sort = by(DESC, SquadMemberHistory::createDate.name)
    ): List<SquadMemberHistory>

    fun findAllByMember(
        member: Member,
        sort: Sort = by(DESC, SquadMemberHistory::createDate.name)
    ): List<SquadMemberHistory>

    fun findAllBySquadAndRevisionNumberAfter(
        squad: Squad,
        revisionNumber: Int,
        sort: Sort = by(DESC, SquadMemberHistory::createDate.name)
    ): List<SquadMemberHistory>

    fun deleteAllByMember(member: Member)

    fun deleteAllBySquad(squad: Squad)
}
