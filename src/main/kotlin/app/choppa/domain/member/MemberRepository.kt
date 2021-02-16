package app.choppa.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberRepository : JpaRepository<Member, UUID> {

    @Query(
        value =
            """
            SELECT DISTINCT member.member_id, member.name, chapter
            FROM member
                     INNER JOIN squad_current_members ON squad_current_members.member_id = member.member_id
                     INNER JOIN squad ON squad.squad_id = squad_current_members.squad_id
            WHERE squad.tribe = cast(:tribeId AS UUID)
        """,
        nativeQuery = true
    )
    fun findAllByTribeId(@Param("tribeId") tribeId: UUID): List<Member>

    @Query(
        value =
            """
            SELECT DISTINCT member.member_id, member.name, chapter
            FROM member
                    INNER JOIN squad_current_members ON squad_current_members.member_id = member.member_id
            WHERE squad_current_members.squad_id = cast(:squadId AS UUID)
        """,
        nativeQuery = true
    )
    fun findAllBySquadId(@Param("squadId") squadId: UUID): List<Member>

    fun findAllByChapterId(chapterId: UUID): List<Member>

    @Modifying
    @Query(
        value =
            """
            DELETE FROM squad_current_members
            WHERE squad_current_members.member_id = cast(:memberId AS UUID)
        """,
        nativeQuery = true
    )
    fun deleteAllSquadMemberRecordsFor(@Param("memberId") memberId: UUID)

    fun findAllByActiveFalse(): List<Member>
}
