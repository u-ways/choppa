package app.choppa.domain.squad.history

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.domain.Sort.by
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SquadMemberHistoryRepository : JpaRepository<SquadMemberHistory, SquadMemberHistoryId> {
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
