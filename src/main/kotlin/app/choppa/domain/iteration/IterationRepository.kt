package app.choppa.domain.iteration

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface IterationRepository : JpaRepository<Iteration, UUID> {
    @Query(
        value =
            """
            SELECT DISTINCT iteration.iteration_id, number, start_date, end_date
            FROM iteration
                    INNER JOIN history on iteration.iteration_id = history.iteration_id
            WHERE history.member_id = cast(:memberId AS UUID)
        """,
        nativeQuery = true
    )
    fun findAllByMemberId(@Param("memberId") memberId: UUID): List<Iteration>

    @Query(
        value =
            """
            SELECT DISTINCT iteration.iteration_id, number, start_date, end_date
            FROM iteration
                    INNER JOIN history on iteration.iteration_id = history.iteration_id
            WHERE history.squad_id = cast(:squadId AS UUID)
        """,
        nativeQuery = true
    )
    fun findAllBySquadId(@Param("squadId") squadId: UUID): List<Iteration>

    @Query(
        value =
            """
            SELECT DISTINCT iteration.iteration_id, number, start_date, end_date
            FROM iteration
                    INNER JOIN history on iteration.iteration_id = history.iteration_id
            WHERE history.tribe_id = cast(:tribeId AS UUID)
        """,
        nativeQuery = true
    )
    fun findAllByTribeId(@Param("tribeId") tribeId: UUID): List<Iteration>
}
