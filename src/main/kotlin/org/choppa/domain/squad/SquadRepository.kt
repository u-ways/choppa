package org.choppa.domain.squad

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SquadRepository : JpaRepository<Squad, UUID> {

    fun findAllByTribeId(tribeId: UUID): List<Squad>

    @Query(
        value =
            """
            SELECT DISTINCT squad.squad_id, name, color, tribe
            FROM squad
                    INNER JOIN squad_current_members ON squad_current_members.squad_id = squad.squad_id
            WHERE squad_current_members.member_id = cast(:memberId AS UUID)
        """,
        nativeQuery = true
    )
    fun findAllByMemberId(@Param("memberId") squadId: UUID): List<Squad>
}
