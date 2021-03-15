package app.choppa.domain.chapter

import app.choppa.domain.account.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChapterRepository : JpaRepository<Chapter, UUID> {

    @Query(
        value =
            """
            SELECT * FROM chapter WHERE chapter_id IN (
            SELECT DISTINCT member.chapter
            FROM member
                     INNER JOIN squad_current_members ON squad_current_members.member_id = member.member_id
                     INNER JOIN squad ON squad.squad_id = squad_current_members.squad_id
            WHERE squad.tribe = cast(:tribeId AS UUID))
        """,
        nativeQuery = true
    )
    fun findAllByTribeId(@Param("tribeId") tribeId: UUID): List<Chapter>

    @Query(
        value =
            """
            SELECT * FROM chapter WHERE chapter_id IN (
            SELECT DISTINCT member.chapter
            FROM member
                     INNER JOIN squad_current_members ON squad_current_members.member_id = member.member_id
            WHERE squad_current_members.squad_id = cast(:squadId AS UUID))
        """,
        nativeQuery = true
    )
    fun findAllBySquadId(@Param("squadId") squadId: UUID): List<Chapter>

    fun findByNameAndAccount(name: String, account: Account): Chapter?
}
