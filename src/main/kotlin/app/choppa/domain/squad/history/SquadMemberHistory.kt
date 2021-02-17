package app.choppa.domain.squad.history

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.time.Instant
import java.time.Instant.now
import javax.persistence.*
import javax.persistence.EnumType.STRING

@Entity
@IdClass(SquadMemberHistoryId::class)
@Table(name = "squad_member_history")
@JsonSerialize(using = SquadMemberHistorySerializer::class)
data class SquadMemberHistory(
    @Id
    @ManyToOne
    @JoinColumn(name = "squad_id", referencedColumnName = "squad_id", columnDefinition = "uuid")
    val squad: Squad,

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", columnDefinition = "uuid")
    val member: Member,

    @Column(name = "revision_number", columnDefinition = "INTEGER")
    val revisionNumber: Int,

    @Enumerated(STRING)
    @Column(name = "revision_type", columnDefinition = "REVISION_TYPE")
    val revisionType: RevisionType,

    @Id
    @Column(name = "create_date", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    val createDate: Instant = now(),
) {
    override fun toString() =
        "SquadMembersHistory(squad=${squad.name}, member=${member.name}, revision=$revisionNumber, type=$revisionType)"

    override fun hashCode(): Int = SquadMemberHistoryId(squad.id, member.id, createDate).hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SquadMemberHistory
        val id = SquadMemberHistoryId(squad.id, member.id, createDate).hashCode()
        val otherId = SquadMemberHistoryId(other.squad.id, other.member.id, other.createDate).hashCode()
        if (id != otherId) return false
        return true
    }
}
